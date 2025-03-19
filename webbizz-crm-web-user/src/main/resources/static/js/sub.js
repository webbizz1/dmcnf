// 공통
window.addEventListener('DOMContentLoaded', () => {
  function isMobileDevice() {
    return window.innerWidth <= 768;
  }

  // 공유 버튼 클릭 시 공유 팝업 표시/숨기기
  // const shareBtn = document.querySelector('.function-wrap .btn-share');
  // const sharePopup = shareBtn.nextElementSibling;

  // shareBtn.addEventListener('click', function (e) {
  //   e.stopPropagation();
  // 	if(sharePopup.classList.contains('on')) {
  // 		sharePopup.classList.remove('on');
  // 	} else {
  // 		sharePopup.classList.add('on');
  // 	}
  //   sharePopup.querySelector('.btn-close').addEventListener('click', function () {
  //     sharePopup.classList.remove('on');
  //   });
  // });

  // 외부 영역 클릭 시 모든 공유 팝업, custom select 등 닫기
  document.addEventListener('click', function (e) {
    // 공통 닫기 함수
    function outsideClickClose(containerEl, hideAction) {
      let container = document.querySelector(containerEl);
      console.log(container.hasChildNodes(e.target), e.target);
      if (!container.hasChildNodes(e.target) && container.has(e.target).length === 0) {
        hideAction();
      }
    }

    // 공유 팝업 닫기
    // outsideClickClose('.function-wrap', function () {
    //   sharePopup.classList.remove('on');
    // });
  });

  function initializeTabs() {
    let tabBtn = document.querySelectorAll(".tab-wrap .tab");

    tabBtn.forEach((tab) => {
      const tabWrap = tab.closest(".tab-wrap");
      const tabSelect = tabWrap.querySelector(".tab-select");
      const activeTab = tabWrap.querySelector(".tab.on");

      if (tabSelect && activeTab) {
        tabSelect.textContent = activeTab.textContent;
      }

      tab.addEventListener("click", (e) => {
        tabWrap.querySelectorAll(".tab").forEach((a) => a.classList.remove("on"));
        tab.classList.add("on");

        if (tabSelect) {
          tabSelect.textContent = tab.textContent;
        }

        if (isMobileDevice()) {
          tabSelect.classList.remove("on"); // 모바일에서 탭 선택 후 닫기
          const tabInner = tabWrap.querySelector(".tab-inner");
          if (tabInner) {
            tabInner.style.height = "0";
          }
        }
      });
    });

    if (isMobileDevice()) {
      document.querySelectorAll(".tab-select").forEach((el) => {
        el.addEventListener("click", toggleTab);
      });
    }
  }

  function toggleTab(event) {
    if (!isMobileDevice()) return; // 768px 이하에서만 실행

    const tabSelect = event.currentTarget;
    const tabWrap = tabSelect.closest(".tab-wrap");
    const tabInner = tabWrap.querySelector(".tab-inner");

    if (!tabInner) return;

    if (tabSelect.classList.contains("on")) {
      tabSelect.classList.remove("on");
      tabInner.style.height = "0";
    } else {
      tabSelect.classList.add("on");
      tabInner.style.height = tabWrap.querySelector(".select-list").offsetHeight + "px";
      tabInner.style.opacity = "1";
    }
  }

  // 화면 크기 변경 시 다시 적용
  window.addEventListener("resize", () => {
    document.querySelectorAll(".tab-select").forEach((el) => {
      el.removeEventListener("click", toggleTab);
    });

    // 화면 크기 변화 후 height 초기화
    document.querySelectorAll(".tab-wrap .tab-inner").forEach((tabInner) => {
      tabInner.style.height = "";  // height 초기화
    });
    
    initializeTabs();
  });

  // 초기 실행
  initializeTabs();

  // 재단 정관
  document.querySelectorAll(".accordion-box .sub-header").forEach(header => {
    header.addEventListener("click", () => {
      const accordion = header.closest(".accordion-box");
      const isActive = accordion.classList.contains("active");

      // 모든 아코디언 닫기
      document.querySelectorAll(".accordion-box").forEach(acc => {
        acc.classList.remove("active");
      });

      if (!isActive) {
        accordion.classList.add("active");
        setTimeout(() => {
          const yOffset = -100;
          const y = accordion.getBoundingClientRect().top + window.scrollY + yOffset;

          window.scrollTo({ top: y, behavior: "smooth" });
        });
      }
    });
  });

  // 연혁
  const historyItems = document.querySelectorAll(".history-list dl");

  function updateActiveHistory() {
    const scrollTop = window.scrollY;
    const windowHeight = window.innerHeight;
    const docHeight = document.body.scrollHeight;
    let activeIndex = 0;

    // 마지막 항목 처리 (스크롤이 끝까지 내려간 경우)
    if (windowHeight + scrollTop >= docHeight - 10) {
      activeIndex = historyItems.length - 1;
    } else {
      historyItems.forEach((item, index) => {
        const itemTop = item.offsetTop - 150; // px 여유를 둠 (조절 가능)
        if (scrollTop >= itemTop) {
          activeIndex = index;
        }
      });
    }

    // 활성화 클래스 업데이트
    historyItems.forEach((item, index) => {
      if (index === activeIndex) {
        item.classList.add("on");
      } else {
        item.classList.remove("on");
      }
    });
  }

  // 초기화 & 스크롤 이벤트 추가
  updateActiveHistory();
  window.addEventListener("scroll", updateActiveHistory);

});
