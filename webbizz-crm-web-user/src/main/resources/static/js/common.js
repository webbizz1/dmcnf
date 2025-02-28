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
        if (error.expired === true) {
            alert('세션이 만료되었습니다. 다시 로그인해 주세요.');
            location.reload();
            return;
        }
    }

    alert(errorMessage);
}

/** 파일 업로드 */
function uploadFiles($inputFile, source) {
    if ($inputFile.files.length === 0) return Promise.reject();

    const inputName = $inputFile.getAttribute('data-name') || 'fileUUIDs';
    const preview = $inputFile.getAttribute('data-preview');
    const maxFiles = parseInt($inputFile.getAttribute('data-max-files') || 99);
    source = source ? source : ($inputFile.getAttribute('data-source') || null);

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
                ...result.data,
                options: {
                    inputName, preview
                }
            };
        })
        .catch(handleFetchError)
        .finally(() => {
            setTimeout(() => {
                $inputFile.value = null;
            }, 100);
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
            if (label !== undefined) label.textContent = '파일을 선택해 주세요.';
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
function replaceTemplate(template, data) {
    const templateString = template.innerHTML;
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
});