document.addEventListener('DOMContentLoaded', () => {
    const $lnb = document.querySelector('#lnb');
    const $toggleButton = document.querySelector('#lnb .openBtn');

    if ($toggleButton !== null) {
        $toggleButton.addEventListener('click', () => {
            if ($lnb.classList.contains('menu-off')) {
                $lnb.classList.remove('menu-off');
                $lnb.nextElementSibling.classList.remove('menu-off');
                $toggleButton.textContent = '<';
                sessionStorage.setItem('menu-off', 'false');
            } else {
                $lnb.classList.add('menu-off');
                $lnb.nextElementSibling.classList.add('menu-off');
                $toggleButton.textContent = '>';
                sessionStorage.setItem('menu-off', 'true');
            }
        });

        if (sessionStorage.getItem('menu-off') === 'true') {
            $lnb.classList.add('menu-off');
            $lnb.nextElementSibling.classList.add('menu-off');
            $toggleButton.textContent = '>';
        }
    }

    const $navBarStart = document.querySelector('.global-navbar .navbar-start');
    const $navbarItemFirst = document.querySelector('.global-navbar .navbar-start .navbar-item:first-child');
    const $navbarItemLast = document.querySelector('.global-navbar .navbar-start .navbar-item:last-child');

    /* GNB 의 첫 번째와 마지막 요소가 화면에 보일 때 배경 그라데이션 효과를 적용 */
    if ($navbarItemFirst !== null && $navbarItemLast !== null) {
        const observer = new IntersectionObserver(entries => {
            entries.forEach(entry => {
                if (entry.target === $navbarItemFirst) {
                    $navBarStart.classList.toggle('is-gradient-left-active', !entry.isIntersecting);
                } else if (entry.target === $navbarItemLast) {
                    $navBarStart.classList.toggle('is-gradient-right-active', !entry.isIntersecting);
                }
            })
        }, { threshold: 0.99 });
        observer.observe($navbarItemFirst);
        observer.observe($navbarItemLast);
    }

    /* GNB 의 활성화된 메뉴가 화면에 보일 때 스크롤 이벤트를 추가하여 가운데로 스크롤 */
    document.querySelectorAll('.global-navbar .navbar-menu').forEach(($el) => {
        const $selected = $el.querySelector('.is-active');
        if ($selected !== null) {
            $el.scrollTo({ left: $selected.offsetLeft - $el.offsetWidth / 2, behavior: 'smooth' });
        }
    });

    /* 모바일 화면에서 햄버거 버튼을 클릭했을 때 네비게이션 메뉴를 토글 */
    document.querySelectorAll('.global-navbar .navbar-burger').forEach(($el) => {
        $el.addEventListener('click', () => {
            const $target = document.querySelector($el.dataset.target);
            const $modal = document.querySelector('.navbar-modal');

            if ($modal === null) {
                const modal = document.createElement('div');
                modal.classList.add('navbar-modal', 'modal', 'is-active');
                const background = document.createElement('div');
                background.classList.add('modal-background');
                modal.appendChild(background);
                modal.addEventListener('click', () => {
                    document.querySelector('.navbar-burger').click();
                });
                document.body.appendChild(modal);
                document.body.style.position = 'fixed';
                document.body.style.overflowY = 'hidden';
                document.body.style.height = '100%';

            } else {
                $modal.remove();
                document.body.style.position = '';
                document.body.style.overflowY = '';
                document.body.style.height = '';
            }

            $el.classList.toggle('is-active');
            $target.classList.toggle('is-active');
        });
    });


    function openModal($el) {
        $el.classList.add('is-active');
        document.querySelector('html').classList.add('is-clipped');
    }

    function closeModal($el) {
        $el.classList.remove('is-active');
        if (document.querySelectorAll('.modal.is-active').length === 0) {
            document.querySelector('html').classList.remove('is-clipped');
        }
    }

    function closeAllModals() {
        document.querySelectorAll('.modal').forEach(($modal) => {
            closeModal($modal);
        });
    }

    /* 특정 모달 팝업을 열기 위해 클래스가 'attach-modal' 인 요소에 클릭 이벤트를 추가 */
    document.querySelectorAll('.attach-modal').forEach(($trigger) => {
        const modal = $trigger.dataset.target;
        const $target = document.getElementById(modal);

        $trigger.addEventListener('click', () => {
            openModal($target);
        });
    });

    /* 하위 요소에 클릭 이벤트를 추가하여 상위 모달 팝업을 닫게 하기 */
    document.querySelectorAll('.modal-background, .modal-close, .modal .delete').forEach(($close) => {
        const $target = $close.closest('.modal');

        $close.addEventListener('click', () => {
            closeModal($target);
        });
    });

    /* 모든 모달 팝업을 닫는 키보드 이벤트 추가 */
    document.addEventListener('keydown', (event) => {
        if (event.key === 'Escape') {
            closeAllModals();
            if (document.querySelector('.navbar-modal') !== null) {
                document.querySelector('.navbar-burger').click();
            }
        }
    });
});
