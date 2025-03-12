// 공통
window.addEventListener('DOMContentLoaded', () => {
  function isMobileDevice() {
		return window.innerWidth <= 1241;
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

  let tabBtn = document.querySelectorAll('.tab-wrap .tab');
  tabBtn.forEach( tab => {
    const tabSelect = tab.closest('.tab-wrap').querySelector('.tab-select');
    tabSelect.textContent = tab.closest('.tab-wrap').querySelector('.tab.on').textContent;

    tab.addEventListener('click', (e) => {
      tabBtn.forEach(a => a.classList.remove('on'));
      tab.classList.add('on');
    });
  });

  function toggleTab(element) {
    const tabSelect = element.target;
    if (tabSelect.classList.contains('on')) {
      tabSelect.nextElementSibling.style.height = '0';
      tabSelect.classList.remove('on');
    } else {
      tabSelect.classList.add('on');
      const tabWrap = tabSelect.closest('.tab-wrap').querySelector('.w-set');
      tabWrap.style.height = tabWrap.querySelector('.select-list').offsetHeight + 'px';
    }
    tabBtn.forEach( tab => {
      tab.addEventListener('click', () => {
        tabSelect.nextElementSibling.style.height = '0';
        tabSelect.classList.remove('on');
        tabSelect.textContent = tab.textContent;
      });
    });
  }

  // document.querySelector('.tab-select').addEventListener('click', (e) => toggleTab(e));

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

});
