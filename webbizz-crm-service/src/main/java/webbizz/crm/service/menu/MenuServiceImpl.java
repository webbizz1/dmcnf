package webbizz.crm.service.menu;

import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.board.BoardRepository;
import webbizz.crm.domain.board.entity.Board;
import webbizz.crm.domain.menu.MenuRepository;
import webbizz.crm.domain.menu.dto.MenuDto;
import webbizz.crm.domain.menu.dto.MenuHierarchyDto;
import webbizz.crm.domain.menu.dto.MenuPageDto;
import webbizz.crm.domain.menu.dto.MenuSaveDto;
import webbizz.crm.domain.menu.entity.Menu;
import webbizz.crm.exception.BusinessException;
import webbizz.crm.util.XssFilterUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("menuService")
@RequiredArgsConstructor
public class MenuServiceImpl extends EgovAbstractServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    private final BoardRepository boardRepository;

    private final Map<Long, List<MenuDto>> cacheMenuDtoMap = new HashMap<>();

    @Value("${server.port}")
    private String serverPort;

    @Override
    public MenuDto searchByIdForUser(final Long id) {
        return menuRepository.searchByIdForUser(id)
                .orElseThrow(() -> new BusinessException(404, "페이지를 찾을 수 없습니다."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuDto> searchAllForNonDeleted(final Long groupId) {
        return this.searchAllForNonDeleted(groupId, false);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuDto> searchAllForNonDeleted(final Long groupId, final boolean isCache) {
        // 캐시 사용하지 않을 경우 바로 조회
        if (!isCache) {
            return menuRepository.searchAllByGroupId(groupId);
        }

        // 캐시를 사용할 경우 먼저 key 값으로 조회한 뒤 없으면 새로 저장 후 반환
        return cacheMenuDtoMap.computeIfAbsent(groupId, key -> menuRepository.searchAllByGroupId(groupId));
    }

    @Override
    @Transactional(readOnly = true)
    public MenuPageDto getMenuPage(final Long id) {
        MenuDto menuDto = this.searchByIdForUser(id);
        List<MenuDto> allMenus = this.searchAllForNonDeleted(menuDto.getGroupId(), true)
                .stream()
                .filter(m -> m.getViewYn().equals(YN.Y))
                .collect(Collectors.toList());

        Map<Long, MenuDto> menuMap = allMenus.stream().collect(Collectors.toMap(MenuDto::getId, Function.identity()));
        MenuDto currentMenu = Optional.ofNullable(menuMap.get(id))
                .orElseThrow(() -> new BusinessException(404, "메뉴를 찾을 수 없습니다."));

        List<MenuDto> parentsMenus = this.getParentMenus(currentMenu, menuMap);
        List<MenuDto> brothers = this.getBrotherMenus(currentMenu, allMenus);
        List<List<MenuDto>> menuBreadcrumbs = this.buildMenuBreadcrumbs(currentMenu, allMenus, new ArrayList<>());

        return new MenuPageDto(menuDto, parentsMenus, brothers, menuBreadcrumbs);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "menuHierarchy", key = "#groupId")
    public List<MenuHierarchyDto> getMenuHierarchy(final Long groupId) {
        return this.buildHierarchy(this.searchAllForNonDeleted(groupId, false));
    }

    @Override
    @CacheEvict(value = "menuHierarchy", allEntries = true)
    public void evictMenuCache(final Long groupId) {
        this.cacheMenuDtoMap.clear();
        egovLogger.info("evictMenuCache groupId = {}", groupId);
    }

    @Override
    @Transactional
    public void saveMenus(final Long groupId, final List<MenuSaveDto> requestDto) {
        // groupId 에 해당하는 모든 메뉴 조회
        List<Menu> nonDeletedMenus = menuRepository.findAllByGroupId(groupId);

        // 저장할 메뉴 리스트 (findAllById 로 반환되는 리스트는 수정 불가능한 리스트이므로 새로운 리스트로 복사)
        List<Menu> saveMenus = new ArrayList<>(
                menuRepository.findAllById(requestDto.stream()
                        .map(MenuSaveDto::getId)
                        .filter(id -> id > 0)
                        .collect(Collectors.toList())));

        // 저장할 게시판 리스트 (연관 관계로 인해 미리 조회)
        List<Board> boards = boardRepository.findAllById(requestDto.stream()
                .filter(dto -> dto.getBoard() != null && dto.getBoard().getId() != null)
                .map(dto -> dto.getBoard().getId())
                .distinct()
                .collect(Collectors.toList()));

        // 신규 메뉴의 시퀀스 매핑 Map (key: 요청 값 (음수), value: 저장된 값 (신규 시퀀스, 양수))
        Map<Long, Long> idMap = new HashMap<>();

        requestDto.forEach(dto -> {
            Menu menu = dto.getId() > 0 ?
                    saveMenus.stream()
                            .filter(m -> ObjectUtils.nullSafeEquals(m.getId(), dto.getId()))
                            .findAny()
                            .orElse(Menu.builder().build())
                    :
                    Menu.builder().build();

            menu
                    .setParentId(dto.getParentId() != null ? dto.getParentId() : null)
                    .setGroupId(groupId)
                    .setDepth(dto.getDepth())
                    .setSortOrder(dto.getSortOrder())
                    .setName(dto.getName())
                    .setType(dto.getType())
                    .setContent(XssFilterUtils.filter(dto.getContent()))
                    .setLinkUrl(dto.getLinkUrl())
                    .setLinkTarget(dto.getLinkTarget())
                    .setSeoKeyword(dto.getSeoKeyword())
                    .setSeoDescription(dto.getSeoDescription())
                    .setManagerDepartment(dto.getManagerDepartment())
                    .setManagerTelephone(dto.getManagerTelephone())
                    .setManagerEmail(dto.getManagerEmail())
                    .setPublicUseYn(dto.getPublicUseYn())
                    .setViewYn(dto.getViewYn());

            // 게시판 연관 관계 설정
            if (dto.getBoard() != null && dto.getBoard().getId() != null) {
                menu.setBoard(boards.stream()
                        .filter(b -> ObjectUtils.nullSafeEquals(b.getId(), dto.getBoard().getId()))
                        .findAny()
                        .orElse(null));
            }

            // id 가 음수인 경우 새로운 메뉴이므로 insert 후 idMap 에 저장하여 요청 값과 매핑
            if (dto.getId() < 0) {
                Long saveId = dto.getId();
                idMap.put(saveId, menuRepository.save(menu).getId());
            }

            saveMenus.add(menu);
        });

        // idMap 을 이용하여 parentMenuId 를 매핑
        saveMenus.forEach(menu -> {
            if (menu.getParentId() != null && menu.getParentId() < 0 && idMap.containsKey(menu.getParentId())) {
                menu.setParentId(idMap.get(menu.getParentId()));
            }
        });

        // 저장되지 않은 메뉴 삭제
        nonDeletedMenus.stream()
                .filter(menu -> saveMenus.stream().noneMatch(m -> ObjectUtils.nullSafeEquals(m.getId(), menu.getId())))
                .forEach(menu -> menu.setDelYn(YN.Y));

        menuRepository.saveAll(saveMenus);
        this.evictMenuCache(groupId);
        this.broadcastMenuEvictCache();
    }

    @Transactional(readOnly = true)
    public MenuDto findByMenuUrlAndGroupId(final String url, final Long groupId) {
        return this.searchAllForNonDeleted(groupId, true).stream()
                .filter(menu -> Objects.nonNull(menu.getLinkUrl()))
                .filter(menu -> ObjectUtils.nullSafeEquals(url, menu.getLinkUrl()))
                .findFirst()
                .orElse(null);
    }

    /**
     * MenuDto 리스트를 계층 구조로 변환
     *
     * @param menuDtos MenuDto 리스트
     * @return 계층 구조로 변환된 MenuHierarchyDto 리스트
     * @author TAEROK HWANG
     */
    private List<MenuHierarchyDto> buildHierarchy(final List<MenuDto> menuDtos) {
        Map<Long, MenuHierarchyDto> menuMap = menuDtos.stream()
                .map(MenuHierarchyDto::new)
                .collect(Collectors.toMap(
                        MenuHierarchyDto::getId,
                        Function.identity(),
                        (existing, replacement) -> existing,
                        LinkedHashMap::new // 입력된 순서 유지하기 위해
                ));

        return menuDtos.stream()
                .filter(dto -> dto.getParentId() == null && dto.getViewYn().isBool())
                .map(dto -> {
                    MenuHierarchyDto rootMenuDto = menuMap.get(dto.getId());
                    buildHierarchyChildren(rootMenuDto, menuMap);
                    return rootMenuDto;
                })
                .collect(Collectors.toList());
    }

    /**
     * 계층 구조로 변환된 MenuHierarchyDto 에 자식 메뉴 추가
     *
     * @param rootMenuDto 계층 구조로 변환된 MenuHierarchyDto
     * @param menuMap     메뉴 Map (key: 메뉴 시퀀스, value: 메뉴객체)
     */
    private void buildHierarchyChildren(final MenuHierarchyDto rootMenuDto, final Map<Long, MenuHierarchyDto> menuMap) {
        menuMap.values().stream()
                .filter(dto -> ObjectUtils.nullSafeEquals(dto.getParentId(), rootMenuDto.getId()))
                .forEach(dto -> {
                    rootMenuDto.getChildren().add(dto);
                    buildHierarchyChildren(dto, menuMap);
                });
    }

    /**
     * 선택한 메뉴와 연관되어 있는 메뉴들을 조회
     *
     * @param menu 현재 선택된 메뉴
     * @param allMenus 전체 메뉴 리스트
     * @param buildMenus 연관된 메뉴 리스트 (재사용)
     * @return 연관된 메뉴 리스트
     * @author TAEROK HWANG
     */
    private List<List<MenuDto>> buildMenuBreadcrumbs(final MenuDto menu,
                                                     final List<MenuDto> allMenus,
                                                     final List<List<MenuDto>> buildMenus) {

        List<MenuDto> brothers = this.getBrotherMenus(menu, allMenus);

        buildMenus.add(0, brothers); // 가장 앞에 넣기 위해 (depth 가 높은 메뉴가 앞으로)

        MenuDto parentFirstMenu = allMenus.stream()
                .filter(m -> ObjectUtils.nullSafeEquals(menu.getParentId(), m.getId()))
                .findAny()
                .orElse(null);

        if (parentFirstMenu != null) {
            return this.buildMenuBreadcrumbs(parentFirstMenu, allMenus, buildMenus);
        }

        return buildMenus;
    }

    /**
     * 현재 메뉴로부터 상위 부모 메뉴 조회 <br />
     * 예: 3depth 메뉴 선택시 -> 1depth > 2depth > 3depth 순으로 정렬된 메뉴 리스트 반환
     *
     * @param menu    현재 선택된 메뉴
     * @param menuMap 전체 메뉴 Map (key: 메뉴 시;퀀스, value: 메뉴객체)
     * @return 해당 메뉴의 부모를 포함한 목록
     * @author YONGHO LEE
     */
    private List<MenuDto> getParentMenus(final MenuDto menu, final Map<Long, MenuDto> menuMap) {
        List<MenuDto> parents = new ArrayList<>();
        MenuDto currentMenu = menu;

        while (currentMenu != null) {
            parents.add(currentMenu);
            currentMenu = currentMenu.getParentId() != null ? menuMap.get(currentMenu.getParentId()) : null;
        }

        return parents.stream()
                .sorted(Comparator.comparing(MenuDto::getDepth)) // 뎁스 오르기순 정렬
                .collect(Collectors.toList());
    }

    /**
     * 메뉴의 형제 메뉴들을 조회 <br />
     * 같은 부모를 가진 메뉴들을 모두 조회
     *
     * @param menu 현재 선택된 메뉴
     * @param allMenus 전체 메뉴 리스트
     * @return 형제 메뉴 리스트
     * @author YONGHO LEE
     */
    private List<MenuDto> getBrotherMenus(final MenuDto menu, final List<MenuDto> allMenus) {
        return allMenus.stream()
                .filter(m -> ObjectUtils.nullSafeEquals(m.getParentId(), menu.getParentId()))
                .sorted(Comparator.comparing(MenuDto::getSortOrder)) // 정렬 순서 오르기순 정렬
//                .sorted(Comparator.comparing(MenuDto::getSortOrder).reversed()) // 뒤집을시 reversed() 추가
                .collect(Collectors.toList());
    }

    /**
     * 메뉴 캐시 삭제를 위한 브로드캐스트
     *
     * @author TAEROK HWANG
     */
    private void broadcastMenuEvictCache() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(final ClientHttpResponse response) {
                return false;
            }
            @Override
            public void handleError(final ClientHttpResponse response) {}
        });

        ArrayList<CompletableFuture<Void>> futures = new ArrayList<>();

        int broadcastPortStart = Integer.parseInt(this.serverPort) / 10 * 10;
        for (int i = broadcastPortStart; i < broadcastPortStart + 2; i++) {
            final int port = i;
            if (i == Integer.parseInt(this.serverPort)) {
                egovLogger.info("broadcastMenuEvictCache skip port = {}", i);
                continue;
            }

            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                egovLogger.info("broadcastMenuEvictCache port = {}", port);
                restTemplate.exchange(
                        String.format("http://localhost:%d/api/v1/internal/menu/evict-cache", port),
                        HttpMethod.GET,
                        null,
                        Void.class);
            });
            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

}
