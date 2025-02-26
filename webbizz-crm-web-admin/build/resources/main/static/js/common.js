const Utils = {
    dateFormat: (data, format) => {
        if (!data?.valueOf()) return '';
        if (format === undefined)
            format = 'yyyy-MM-dd HH:mm:ss';

        let weekName = ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'];
        let weekNameMin = ['일', '월', '화', '수', '목', '금', '토'];
        let d = typeof data === 'object' ? data : new Date(data);

        return format.replace(/(yyyy|yy|MM|dd|E|e|HH|hh|mm|ss|a\/p)/gi, $1 => {
            switch ($1) {
                case 'yyyy': return String(d.getFullYear());
                case   'yy': return String(d.getFullYear() % 100).padStart(2, '0');
                case   'MM': return String(d.getMonth() + 1).padStart(2, '0');
                case   'dd': return String(d.getDate()).padStart(2, '0');
                case    'E': return weekName[d.getDay()];
                case    'e': return weekNameMin[d.getDay()];
                case   'HH': return String(d.getHours()).padStart(2, '0');
                case   'hh': return String(d.getHours() % 12 === 0 ? 12 : d.getHours() % 12);
                case   'mm': return String(d.getMinutes()).padStart(2, '0');
                case   'ss': return String(d.getSeconds()).padStart(2, '0');
                case  'a/p': return d.getHours() < 12 ? '오전' : '오후';
                default    : return $1;
            }
        });
    },
    telephoneFormat: (inputString) => {
        let cleaned = ('' + inputString).replace(/\D/g, '');
        let regExp = /^(01[016789]|02|03[1-3]|04[1-4]|050[1-9]|05[1-5]|06[1-4]|0[78]0|13[0-9]{2}|1[56][0-9]{2})([0-9]+)?([0-9]{4})/;
        let match = cleaned.match(regExp);

        if (/^[01]/.test(cleaned) === false) return '';
        if (match === null) return cleaned.substring(0, 8);

        if (match[2] !== undefined && match[2].length > 4) {
            match[3] = match[2].substring(4) + match[3].substring(0, 3);
            match[2] = match[2].substring(0, 4);
        }

        if (match[3] !== undefined && match[3].length > 4) {
            match[3] = match[3].substring(0, 4);
        }

        if (match[1].startsWith('1') === true && match[2] !== undefined) {
            match[3] = match[2].substring(0) + match[3].substring(0, 3);
            match[2] = undefined;
        }

        return [match[1], match[2], match[3]].filter(Boolean).join('-');
    },
    priceFormat: (number) => {
        // 숫자를 문자열로 변환
        let numberString = '' + number;
        // 정수부분과 소수점 이하 부분으로 분리
        let parts = numberString.split('.');
        // 정규 표현식을 사용하여 정수부분에 세 자리마다 콤마 추가
        parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        // 소수점 이하 부분과 합쳐서 반환
        return parts.join('.');
    },
    parseJwt: (token) => {
        let splitToken = token.split('.');
        if (splitToken.length !== 3)
            return {};

        return JSON.parse(decodeURIComponent(atob(splitToken[1]).split('').map(chr =>
            '%' + ('00' + chr.charCodeAt(0).toString(16)).slice(-2)).join('')));
    },
    stripNumber: (inputString) => {
        return ('' + inputString).replace(/\D/g, '');
    },
    getCookie: (key) => {
        let value = document.cookie.match('(^|;) ?' + key + '=([^;]*)(;|$)');
        return value !== null ? value[2] : null;
    },
    setCookie: (key, value, options = {}) => {
        options = {
            path: '/',
            ...options
        };

        if (options.expires instanceof Date)
            options.expires = options.expires.toUTCString();

        let updatedCookie = encodeURIComponent(key) + "=" + encodeURIComponent(value);

        for (let optionKey in options) {
            updatedCookie += "; " + optionKey;
            let optionValue = options[optionKey];
            if (optionValue !== true)
                updatedCookie += "=" + optionValue;
        }

        document.cookie = updatedCookie;
    },
    removeCookie: (key) => Utils.setCookie(key, '', { 'max-age': -1 }),
    toggleItem(arr, value) {
        let newArr = (arr === undefined || arr === null) ? [] : (Array.isArray(arr) ? [...arr] : [arr]);

        newArr.includes(value) ? (newArr = newArr.filter(item => item !== value)) : newArr.push(value);

        return newArr;
    },
    removeEmptyKeys: (obj) => {
        let newObj = {};

        for (let key in obj) {
            if (obj[key] !== undefined && obj[key] !== null && obj[key] !== '')
                newObj[key] = obj[key];
        }

        return newObj;
    },
    address: function (zipcode, addr, detail) {
        if (zipcode && detail) {
            return zipcode + ' ) ' + addr + ',' + detail
        } else if (zipcode) {
            return zipcode + ' ) ' + addr
        } else if (detail) {
            return addr + ',' + detail
        } else {
            return addr
        }
    },
    loadExternalScript: function (src) {
        return new Promise((resolve, reject) => {
            if (document.querySelector(`script[src="${src}"]`)) {
                resolve();
                return;
            }

            const script = document.createElement('script');
            script.src = src;
            script.onload = resolve;
            script.onerror = reject;
            document.head.appendChild(script);
        });
    },
    isMobile: () => {
        return /Android|iPhone/.test(navigator.userAgent);
    }
};

function handleFetchResponse(response) {
    if (!response.ok) {
        return response.json()
            .then(result => { throw result; })
            .catch(error => { throw error; });
    }
    if (response.ok && response.redirected === true && response.url.includes('/login') === true) {
        throw { expired: true };
    }

    return response.json();
}
function handleFetchError(error) {
    console.error('error', error);
    let errorMessage = '네트워크 오류가 발생했습니다.';

    if (error !== undefined) {
        if (Array.isArray(error.errors) === true) {
            errorMessage = error.errors.map(err => `- ${err.message}`).join('\n');
        }
        else if (error.message !== undefined) {
            errorMessage = error.message;
        }
        else if (error.error !== undefined) {
            errorMessage = error.error;
        }
        else if (typeof error === 'string') {
            errorMessage = error;
        }
        if (error.expired === true) {
            alert('세션이 만료되었습니다. 다시 로그인해 주세요.');
            location.reload();
            return;
        }
    }

    alert(errorMessage);
}
function handleExcelExport(response) {
    if (response.status !== 200) {
        console.error(response);
        throw new Error(`엑셀 다운로드에 실패했습니다: ${response.status} Error`);
    }

    let filename = '';
    const disposition = response.headers.get('Content-Disposition');
    filename = disposition.split(/;(.+)/)[1].split(/=(.+)/)[1];
    if (filename.toLowerCase().startsWith("utf-8''"))
        filename = decodeURIComponent(filename.replace(/utf-8''/i, ''));
    else
        filename = filename.replace(/['"]/g, '');

    response.blob().then(blob => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = filename;
        a.click();
    });
}

/**
 * <p>파일 업로드</p>
 * <p>
 * data-name: input 태그의 name 속성 <br />
 * data-source: 파일 업로드 소스 (ex. 'fileUUID') <br />
 * data-preview: 파일 업로드 후 미리보기를 표시할 DOM ID (# 없음) <br />
 * data-max-files: 최대 업로드 가능한 파일 수 (기본값: 99) <br />
 * </p>
 *
 * @param $inputFile 파일 업로드 input 태그 (DOM Element)
 * @param source 파일 업로드 소스 (변경할 경우)
 */
function uploadFiles($inputFile, source) {
    if ($inputFile.files.length === 0) return Promise.reject();

    const inputName = $inputFile.dataset.name || 'fileUUIDs';
    const preview = $inputFile.dataset.preview;
    const maxFiles = parseInt($inputFile.dataset.maxFiles || 99);
    source = source ? source : ($inputFile.dataset.source || null);

    const uploadedFileCount = document.querySelectorAll(`#${preview} [data-uuid]`).length;
    if ($inputFile.files.length + uploadedFileCount > maxFiles) {
        alert(`최대 ${maxFiles}개까지 업로드 가능합니다.`);
        setTimeout(() => {
            $inputFile.value = null;
        }, 100);
        return Promise.reject();
    }

    const formData = new FormData();
    if (source != null) {
        formData.append('source', source);
        Array.prototype.forEach.call($inputFile.files, file => formData.append('files', file));
    }

    return fetch('/api/v1/attachment', {
        method: 'POST',
        body: formData
    })
        .then(handleFetchResponse)
        .then(result => {
            return {
                data: result.data,
                options: { inputFile: $inputFile, source, inputName, preview }
            };
        })
        .catch(handleFetchError)
        .finally(() => {
            setTimeout(() => {
                $inputFile.value = null;
            }, 100);
        });
}

/** 파일 업로드 후 DOM 에 추가 (일반 템플릿) */
function handleUploadFilesResult(result, templateId) {
    return new Promise((resolve, reject) => {
        const $template = templateId !== undefined
            ? document.querySelector(`#${templateId}`)
            : new DOMParser().parseFromString(`
                <div data-uuid="{{uuid}}">
                    <div class="tags has-addons">
                        <span class="tag is-info is-light">{{originalName}}</span>
                        <a class="tag is-delete" onclick="deleteFile('{{uuid}}', true);"></a>
                        <input type="hidden" name="{{inputName}}" value="{{uuid}}" />
                    </div>
                </div>`, 'text/html').body;
        const $preview = document.getElementById(result.options.preview);

        if ($template === null || $preview === null) {
            console.error('템플릿 또는 미리보기를 표시할 DOM 이 존재하지 않습니다.');
            reject('템플릿 또는 미리보기를 표시할 DOM 이 존재하지 않습니다.');
        }

        const html = result.data
            .map(data => replaceTemplate($template, {
                ...data,
                ...result.options
            }))
            .join('');

        $preview.insertAdjacentHTML('beforeend', html);
        resolve(result);
    });
}

/** 파일 업로드 후 DOM 에 추가 (이미지 미리보기 템플릿) */
function handleUploadFilesImageResult(result, templateId) {
    return new Promise((resolve, reject) => {
        const $template = templateId !== undefined
            ? document.querySelector(`#${templateId}`)
            : new DOMParser().parseFromString(`
                <figure class="image mt-2" style="max-width: 214px" data-uuid="{{uuid}}">
                    <img src="data:image/gif;base64,R0lGODlhAQABAAD/ACwAAAAAAQABAAACADs=" data-src="{{base64}}" alt="{{originalName}}" />
                    <button type="button" class="delete is-small" onclick="deleteFile('{{uuid}}', true)"></button>
                    <input type="hidden" name="{{inputName}}" value="{{uuid}}" />
                </figure>`, 'text/html').body;
        const $preview = document.getElementById(result.options.preview);

        if ($template === null || $preview === null) {
            console.error('템플릿 또는 미리보기를 표시할 DOM 이 존재하지 않습니다.');
            reject('템플릿 또는 미리보기를 표시할 DOM 이 존재하지 않습니다.');
        }

        if (result.data.length === 0) {
            resolve();
        }

        const reader = new FileReader();
        reader.readAsDataURL(result.options.inputFile.files[0]);
        reader.onload = () => {
            const html = replaceTemplate($template, {
                ...result.data[0],
                ...result.options,
                base64: reader.result
            });

            $preview.insertAdjacentHTML('beforeend', html);
            const label = result.options.inputFile.closest('label')?.querySelector('.file-name');
            if (label) label.textContent = result.data[0].originalName;
            resolve(result);
        };
    });
}


/** 파일 삭제 */
function deleteFile(uuid, isActual = false) {
    if (confirm('파일을 삭제하시겠습니까?') === false)
        return;

    const $elements = document.querySelectorAll(`[data-uuid="${uuid}"]`);
    const removeElements = () => {
        $elements.forEach($element => {
            const label = $element.closest('.field')?.querySelector('.file-name');
            if (label) label.textContent = '파일을 선택해 주세요.';
            $element.remove();
        });
    }

    if (isActual === true) {
        return fetch(`/api/v1/attachment/${uuid}`, {
            method: 'DELETE'
        })
            .then(handleFetchResponse)
            .then(result => {
                removeElements();
                return true;
            })
            .catch(handleFetchError);
    } else {
        removeElements();
        return Promise.resolve(true);
    }
}

/** 템플릿 문자열 치환 */
function replaceTemplate($template, data) {
    const templateString = $template.innerHTML;
    const pattern = /{{\s*(\w+)\s*}}/g;

    let html = templateString.replace(pattern, (match, key) => data[key] || '');
    return html.replace("src=\"data:image/gif;base64,R0lGODlhAQABAAD/ACwAAAAAAQABAAACADs=\" data-", "");
}

document.addEventListener('DOMContentLoaded', () => {
    const telephoneNumberElements = document.querySelectorAll('[data-telephone]');
    telephoneNumberElements.forEach((element) => {
        if (typeof element.value === 'string') {
            element.value = Utils.telephoneFormat(element.value);
            element.addEventListener('input', (event) => {
                event.target.value = Utils.telephoneFormat(event.target.value);
            });
        }
        else {
            element.textContent = Utils.telephoneFormat(element.dataset.telephone);
        }
    });

    document.querySelectorAll('.datepicker-single').forEach(($container) => {
        $container.querySelectorAll('input[type="text"]').forEach(($input) => {
            new Datepicker($input, {
                autohide: true,
                language: 'ko',
                todayHighlight: true,
                clearButton: true,
                todayButton: true,
                todayButtonMode: 1, // select
            });
            $input.autocomplete = 'off';
        });
    });
    document.querySelectorAll('.datepicker-range').forEach(($container) => {
        new DateRangePicker($container, {
            autohide: true,
            language: 'ko',
            todayHighlight: true,
            clearButton: true,
            todayButton: true,
            todayButtonMode: 1, // select
            allowOneSidedRange: true,
        });
        $container.rangepicker.inputs.forEach(($element) => $element.autocomplete = 'off');
    });

    // 최근 방문 메뉴 저장
    const currentMenu = Array.from(document.querySelectorAll('aside.aside ul.menu-list li a.is-active')).slice(-1)[0];
    if (currentMenu !== undefined) {
        const menuItem = {
            name: currentMenu.textContent,
            url: currentMenu.href,
            timestamp: new Date().getTime()
        };

        let visitedMenus = JSON.parse(localStorage.getItem('visitedMenus')) || [];
        visitedMenus = visitedMenus.filter(item => item.url !== menuItem.url);
        visitedMenus.unshift(menuItem);
        localStorage.setItem('visitedMenus', JSON.stringify(visitedMenus.slice(0, 3)));
    }
});

document.addEventListener('alpine:init', () => {
    Alpine.store('notificationComponent', {
        items: [],
        container: null,

        init() {
            this.container = this.createContainer();
            window.$notify = this.notify.bind(this);
        },

        notify(message, type = 'info', duration = 5000) {
            const id = Date.now();
            const notification = {
                id,
                message,
                type,
                visible: true,
                tid: setTimeout(() => this.remove(id), duration)
            };
            this.items.push(notification);
            this.renderNotification(notification);
        },
        remove(id) {
            const index = this.items.findIndex(item => item.id === id);
            if (index > -1) {
                this.items[index].visible = false;
                clearTimeout(this.items[index].tid);
                setTimeout(() => {
                    this.removeNotification(id);
                }, 1000);
            }
        },

        createContainer() {
            const container = document.createElement('div');
            container.className = 'notification-container is-flex is-align-items-self-end is-flex-direction-column is-gap-1 is-position-fixed';
            container.style.cssText = 'z-index: 1000; top: 1.5rem; right: 1.5rem';
            document.body.appendChild(container);
            return container;
        },

        renderNotification(notification) {
            const el = document.createElement('div');
            el.id = `notification-${notification.id}`;
            el.innerHTML = this.template(notification);
            this.container.appendChild(el);
        },
        removeNotification(id) {
            const el = document.getElementById(`notification-${id}`);
            if (el !== null) el.remove();
            this.items = this.items.filter(item => item.id !== id);
        },

        template(notification) {
            return `
                <div x-data="{ notification: $store.notificationComponent.items.find(item => item.id === ${notification.id}) }"
                    x-show="notification.visible"
                    x-transition.opacity.duration.1000ms
                    class="button is-${notification.type} is-align-items-flex-start has-text-left has-text-weight-normal px-4 py-3"
                    style="min-width: 200px; max-width: calc(100vw - 4rem); white-space: normal"
                    @click="$store.notificationComponent.remove(${notification.id})">
                    <span class="icon">
                        <i class="fa-solid fa-circle-info" aria-hidden="true"></i>
                    </span>
                    <p class="mr-1" x-text="notification.message"></p>
                </div>
            `;
        }
    });
});
