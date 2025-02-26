const $handleStorage = {
    // 스토리지에 데이터 쓰기(이름, 만료일)
    setStorage: function (name, exp) {
        // 만료 시간 구하기(exp를 ms단위로 변경)
        var date = new Date();
        date = date.setTime(date.getTime() + exp * 24 * 60 * 60 * 1000);

        // 로컬 스토리지에 저장하기
        // (값을 따로 저장하지 않고 만료 시간을 저장)
        localStorage.setItem(name, date);
    },
    // 스토리지 읽어오기
    getStorage: function (name) {
        var now = new Date();
        now = now.setTime(now.getTime());
        // 현재 시각과 스토리지에 저장된 시각을 각각 비교하여
        // 시간이 남아 있으면 true, 아니면 false 리턴
        return parseInt(localStorage.getItem(name)) > now;
    },
};

const $popup = {
    scrollY: 0, // 스크롤 위치
    zIndex: 10000, // z-index 조절 필요시 값 변경
    count: 0,
    activePopup: [],
    $mask: `<div class="wbz-popup-bg"></div>`,
    maskInitialize: function () {
        // 최초 생성인 경우 팝업
        if (this.count === 0) {
            this.scrollY = window.scrollY; // 최초 팝업 띄울 때 스크롤 위치 저장

            document.body.insertAdjacentHTML('beforeend', this.$mask);
            document.body.style.overflow = 'hidden';
            document.body.style.position = 'fixed';
            document.body.style.top = `-${this.scrollY}px`;
            document.body.style.width = '100%';
            document.querySelector('.wbz-popup-bg').style.zIndex = this.zIndex;
        }
        this.count++;
    },
    stackControl: function(isOpen) {
        let stackLength = this.activePopup.length;
        if (isOpen) {
            if (stackLength > 1) {
                document.querySelector(this.activePopup[stackLength - 2]).classList.remove('on');
            }
        } else {
            if (stackLength != 0) {
                document.querySelector(this.activePopup[stackLength - 1]).classList.add('on');
            }
        }
    },
    alert: function (...params) {
        this.maskInitialize();

        const popupId = `wbz-popup-${this.zIndex + this.count}`;
        const options = {};

        if (params.length < 1)
            throw new Error('오류 발생: alert 내용이 없음');

        params.forEach(param => {
            if (typeof param === 'string') // 문자열로 입력한 경우 content 영역으로 간주
                options.content = param;
            else if (typeof param === 'object') // 객체로 입력한 경우 옵션 추가
                Object.assign(options, param); // options 변수에 param 값의 속성 덧붙이기
            else
                throw new Error('오류 발생: 잘못된 파라미터 요청');
        });

        // 타이틀 입력되지 않은 경우 빈값 처리
        if (!options.title) options.title = '';

        // confirm 타입인 경우
        let buttonTemplate;
        if (options.isConfirm === true) {
            options.modal = true; // confirm 타입이면 강제로 modal 지정
            buttonTemplate = `
				<button type="button" class="cr_2 popup-close">취소</button>
				<button type="button" class="cr_1 popup-confirm-event">확인</button>
			`;
        } else {
            if (options.modal === undefined) options.modal = false; // modal 속성이 지정되어있지 않은 경우에만 속성을 변경
            buttonTemplate = `
				<button type="button" class="cr_1 popup-close">확인</button>
			`;
        }

        // instant 팝업 템플릿
        let template = `
			<div class="wbz-popup-cont is-instant on type-alert" id="${popupId}" style="z-index: ${this.zIndex + this.count}">
				<div class="popup-cont">
					<div class="popup-text">
						${options.content}
					</div>
					<div class="popup-button">
						${buttonTemplate}
					</div>
				</div>
			</div>
		`;
        document.body.insertAdjacentHTML('beforeend', template);
        this.activePopup.push(`#${popupId}`);
        this.stackControl(true);

        // 화면 아래에서 슬라이드 되는 팝업인 경우 애니메이션 적용
        if (document.getElementById(popupId).classList.contains('type-bottom'))
            setTimeout(() => document.getElementById(popupId).classList.add('ani'), 10);

        // 모달 이벤트 설정
        if (options.modal === false)
            document.querySelector(`.wbz-popup-bg`).addEventListener('click', () => $popup.close(true));
        else
            document.getElementById(popupId).classList.add('is-modal');

        // 버튼 이벤트 설정
        document.querySelectorAll(`#${popupId} .popup-confirm-event`).forEach($button => {
            if (typeof options.confirm === 'function')
                $button.addEventListener('click', () => options.confirm());
            else
                throw new Error('오류 발생: $popup.confirm 이벤트가 정의되지 않음');
        });
        document.querySelectorAll(`#${popupId} .popup-close`).forEach($button => {
            $button.addEventListener('click', () => {
                if (typeof options.close === 'function')
                    options.close();
                else
                    this.close();
            });
        });
    },
    confirm: function (...params) {
        let options;
        if (typeof params[0] === 'string')
            options = {
                content: params[0],
                ...params[1]
            };
        else if (typeof params[0] === 'object')
            options = { ...params[0] };
        else
            throw new Error('오류 발생: $popup.confirm 에 잘못된 파라미터가 호출됨');

        options.isConfirm = true; // confirm 타입으로 강제 지정
        this.alert(options);
    },
    template: function (target) {
        this.maskInitialize();

        document.querySelector(target).classList.add('on');
        if (document.querySelector(target).classList.contains('type-bottom')) {
            setTimeout(function () {
                document.querySelector(target).classList.add('ani');
            }, 1);
        }
        document.querySelector(target).style.zIndex = '' + (this.zIndex + this.count);
        this.activePopup.push(target);
        this.stackControl(true);

        let bg = document.querySelector('.wbz-popup-bg');
        if (this.activePopup.length == 1) {
            document.querySelector('.wbz-popup-bg').addEventListener('click', function () {
                $popup.close(true);
            });
        }
    },
    close: function (isMask) {
        let $activePopup = document.querySelector(this.activePopup[this.activePopup.length - 1]);
        if ($activePopup == null) throw new Error('오류 발생: 활성화 팝업이 없음');

        // 모달 팝업이 띄워져 있는 경우 mask 영역을 누르면 닫지 않음
        if (isMask === true && $activePopup.classList.contains('is-modal')) return false;

        if (this.count === 1) {
            document.querySelector('.wbz-popup-bg').remove();

            const body = document.body;
            body.style.removeProperty('overflow');
            body.style.removeProperty('position');
            body.style.removeProperty('top');
            body.style.removeProperty('width');
            window.scrollTo(0, this.scrollY); // 팝업 띄울 당시의 스크롤 위치로 복귀
        }
        if ($activePopup.classList.contains('type-bottom'))
            setTimeout(() => $activePopup.classList.remove('ani'), 10);
        else
            $activePopup.classList.remove('on');

        // instant 팝업인 경우 애니메이션 종료 후 element 삭제
        if ($activePopup.classList.contains('is-instant'))
            setTimeout(() => $activePopup.remove(), 400);

        this.activePopup.pop();
        this.stackControl(false);
        this.count--;
        return true;
    },
    closeAll: function () {
        let activeCount = this.activePopup.length;
        for (let i = 0; i < activeCount; i++) {
            this.close();
        }
        this.activePopup = [];
        this.count = 0;
    }
}

// 팝업 이벤트 리스너 등록
window.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('[data-popup^="#"][data-popup$="-pop"]').forEach(element => {
        element.addEventListener('click', () => $popup.template(element.dataset.popup));
    });
});
