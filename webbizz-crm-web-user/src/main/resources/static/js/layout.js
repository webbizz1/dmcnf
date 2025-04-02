function layoutEvent() {

	const container = document.querySelector('#container');
	const mainContent = container?.querySelector('.main-content');
	const header = document.querySelector('#header');

	if (container && mainContent && header) {
		header.style.position = 'fixed';

		const applyHeaderStyles = () => {
			if (window.innerWidth >= 1025) {
				header.classList.add('on');

				const updateHeaderBackground = () => {
					if (window.scrollY > 0 || header.matches(':hover')) {
						header.classList.remove('on');
					} else {
						header.classList.add('on');
					}
				};

				window.addEventListener('scroll', updateHeaderBackground);
				header.addEventListener('mouseenter', updateHeaderBackground);
				header.addEventListener('mouseleave', updateHeaderBackground);
			} else {
				header.classList.remove('on');
			}

		};

		applyHeaderStyles();
		window.addEventListener('resize', applyHeaderStyles);
	}

	// header gnb
	function gnbEvent(event) {
		const depth1 = event.target.closest('.gnb > li > a');
		const depth3 = event.target.closest('.gnb .depth3 > li > a');

		if (depth1) {
			const nextElement = depth1.nextElementSibling;
			if (nextElement) {
				nextElement.classList.add('on');
				depth1.parentElement.addEventListener('mouseleave', function () {
					nextElement.classList.remove('on');
				});
			}
		}
		if (depth3) {
			const depthElement = depth3.nextElementSibling;
			if (depthElement) {
				depthElement.classList.add('on');
				depth3.parentElement.addEventListener('mouseleave', function () {
					depthElement.classList.remove('on');
				});
			}
		}
	}
	document.addEventListener('mouseover', gnbEvent);
	document.addEventListener('focusin', gnbEvent);

	// language
	// const languageBtn = document.querySelector('.hd-utils .language-wrap .current-lang');
	// const languageParent = languageBtn.parentElement;
	// languageBtn.addEventListener('click', function () {
	// 	if(languageParent.classList.contains('on')) {
	// 		languageParent.classList.remove('on');
	// 	} else {
	// 		languageParent.classList.add('on');
	// 	}
	// });

	// 통합검색창
	// const searchBtn = document.querySelector('.hd-utils .btn-search');
	// const headSearch = document.querySelector('.hd-search-wrap');
	// searchBtn.addEventListener('click', function () {
	// 	if (headSearch.classList.contains('on')) {
	// 		headSearch.classList.remove('on');
	// 	} else {
	// 		headSearch.classList.add('on');
	// 	}
	// 	headSearch.querySelector('.search-close').addEventListener('click', function () {
	// 		headSearch.classList.remove('on');
	// 	});
	// });

	// aside
	// 사이트맵
	const mapBtn = document.querySelector('.all-menu');
	const siteMap = document.querySelector('#aside-wrap');
	const siteMapBG = document.querySelector('.aside-bg');
	const gnb = document.querySelector('.gnb-wrap .gnb');

	function isMobileDevice() {
		return window.innerWidth <= 1023;
	}

	function updatePcGnbDisplay() {
		if (!siteMap) return;

		const isMapOpen = siteMap.classList.contains('on');
		gnb.style.display = isMapOpen ? 'none' : 'flex';
	}

	function updateMobileGnbDisplay() {
		if (!siteMap) return;

		// const loginBtn = document.querySelector('.hd-utils .btn-login');
		// const isMapOpen = siteMap.classList.contains('on');
		gnb.style.display = 'none';
		// loginBtn.style.display = isMapOpen ? 'none' : 'block';
	}

	function updateGnbDisplay() {
		if (isMobileDevice()) {
			updateMobileGnbDisplay();
		} else {
			updatePcGnbDisplay();
		}
	}

	function toggleDepth3Visibility(li) {
		const dep3Ul = li.querySelector('.dep3-ul');
		if (!dep3Ul) return;

		if (isMobileDevice()) {
			if (li.classList.contains('on')) {
				dep3Ul.style.height = '0px';
				dep3Ul.style.overflow = 'hidden';
				dep3Ul.style.visibility = 'hidden';
				li.classList.remove('on');
			} else {
				dep3Ul.style.height = `${dep3Ul.scrollHeight}px`;
				dep3Ul.style.overflow = 'visible';
				dep3Ul.style.visibility = 'visible';
				li.classList.add('on');
			}
		} else {
			dep3Ul.style.height = 'auto';
			dep3Ul.style.overflow = 'visible';
			dep3Ul.style.visibility = 'visible';
			li.classList.add('on');
		}
	}

	function initializeDepth3() {
		document.querySelectorAll('#aside-wrap .dep2-ul > li').forEach((depth) => {
			const dep3Ul = depth.querySelector('.dep3-ul');
			if (dep3Ul) {
				if (!isMobileDevice()) {
					depth.classList.add('on');
					dep3Ul.style.height = 'auto';
					dep3Ul.style.overflow = 'visible';
					dep3Ul.style.visibility = 'visible';
				} else {
					depth.classList.remove('on');
					dep3Ul.style.height = '0px';
					dep3Ul.style.overflow = 'hidden';
					dep3Ul.style.visibility = 'hidden';
				}
			}
		});
	}

	function resetHoverState() {
		const links = document.querySelectorAll('#aside-wrap .dep2-ul > li > a, #aside-wrap .dep3-ul > li > a');
		links.forEach(link => {
			link.style.transition = 'none';
			link.style.color = '';
			link.style.borderColor = '';
			setTimeout(() => {
				link.style.transition = '';
			}, 0);
		});
	}

	function moDepth2(e) {
		e.preventDefault();
		const depth2 = e.currentTarget;
		const parentLi = depth2.parentElement;

		if (!parentLi.querySelector('.dep3-ul')) return;

		if (isMobileDevice()) {
			const siblings = parentLi.parentElement.children;
			Array.from(siblings).forEach(sibling => {
				if (sibling !== parentLi) {
					const siblingDep3Ul = sibling.querySelector('.dep3-ul');
					if (siblingDep3Ul) {
						siblingDep3Ul.style.height = '0px';
						siblingDep3Ul.style.overflow = 'hidden';
						siblingDep3Ul.style.visibility = 'hidden';
						sibling.classList.remove('on');
					}
				}
			});
		}

		toggleDepth3Visibility(parentLi);
	}

	function preventMobileLinkNavigation(e) {
		if (isMobileDevice()) {
			// e.preventDefault();
		}
	}

	document.querySelectorAll('#aside-wrap .dep1').forEach((dep1) => {
		const dep2Wrap = dep1.querySelector('.dep2-wrap');
		if (dep2Wrap) {
			dep1.querySelector('.dep1-tit').addEventListener('click', (e) => {
				e.preventDefault();
				if (dep1.classList.contains('on')) return;

				document.querySelectorAll('#aside-wrap .dep1').forEach((otherDep1) => {
					if (otherDep1 !== dep1) {
						otherDep1.classList.remove('on');
						otherDep1.querySelector('.dep2-wrap')?.classList.remove('on');
						const activeItem = otherDep1.querySelector('li.on');
						if (activeItem) {
							toggleDepth3Visibility(activeItem);
						}
					}
				});

				dep1.classList.add('on');
				dep2Wrap.classList.toggle('on');
			});
		}
	});

	document.querySelectorAll('#aside-wrap .dep2-ul > li').forEach((depth) => {
		const dep3Ul = depth.querySelector('.dep3-ul');
		if (dep3Ul) {
			depth.querySelector('a').addEventListener('click', moDepth2);
		}
	});

	document.querySelectorAll('#aside-wrap a').forEach(link => {
		link.addEventListener('click', preventMobileLinkNavigation);
	});

	const firstDepth1 = document.querySelector('#aside-wrap #sitemap > ul').firstElementChild;
	firstDepth1?.classList.add('on');
	firstDepth1?.querySelector('.dep2-wrap').classList.add('on');

	function siteMapToggle(event) {
		event.preventDefault();
		if (!siteMap) return;

		const isOpen = siteMap.classList.contains('on');
		if (isOpen) {
			document.body.style.removeProperty('overflow');
			mapBtn.classList.remove('active');
			siteMapBG.classList.remove('on');
			siteMap.classList.remove('on');
		} else {
			document.body.style.overflow = 'hidden';
			mapBtn.classList.add('active');
			siteMapBG.classList.add('on');
			siteMap.classList.add('on');
		}

		updateGnbDisplay();
	}

	mapBtn?.addEventListener('click', siteMapToggle);
	siteMapBG?.addEventListener('click', siteMapToggle);

	window.addEventListener('resize', () => {
		updateGnbDisplay();
		initializeDepth3();
		resetHoverState();
	});

	updateGnbDisplay();
	initializeDepth3();

	// $(window).resize(function() {
	// 	isMobileDevice();
	//
	// 	if (isMobileDevice()) {
	// 		allMenuBtn.addEventListener('click', function () {
	// 			if(allMenuBtn.classList.contains('active')) {
	// 				allMenuBtn.classList.remove('active');
	// 				asideControl(false);
	// 			} else {
	// 				allMenuBtn.classList.add('active');
	// 				asideControl(true);
	// 			}
	// 		});
	// 	} else {
	// 		allMenuBtn.addEventListener('click', function () {
	// 			location.href = './sitemap.html';
	// 		});
	// 	}
	// });
	// $(window).trigger('resize');
	//
	// document.querySelector('.aside-bg')?.addEventListener('click', function () {
	// 	asideControl(false);
	// });
	//
	// function asideControl(type) {
	// 	let body = document.body;
	//
	// 	if (type) {
	// 		document.querySelector('.aside-bg').classList.add('on');
	// 		document.querySelector('#aside-wrap').classList.add('on');
	// 		body.style.overflow = 'hidden'; // body 스크롤 잠금
	// 	} else {
	// 		allMenuBtn.classList.remove('active');
	// 		asideDepth2.forEach(a => a.classList.remove('on'));
	// 		document.querySelector('.aside-bg').classList.remove('on');
	// 		document.querySelector('#aside-wrap').classList.remove('on');
	// 		body.style.overflow = ''; // body 스크롤 해제
	// 	}
	// }
	// const firstDepth1 = document.querySelector('#aside-wrap .depth-list').firstElementChild;
	// firstDepth1.firstElementChild.classList.add('on');
	//
	// let asideDepth1 = document.querySelectorAll('#aside-wrap .depth-list > li > a');
	// asideDepth1.forEach(depth1 => {
	// 	depth1.addEventListener('click', function () {
	// 		asideDepth1.forEach(a => a.classList.remove('on'));
	// 		asideDepth2.forEach(a => a.classList.remove('on'));
	// 		depth1.classList.add('on');
	// 	});
	// });
	//
	// let asideDepth2 = document.querySelectorAll('#aside-wrap .depth2 > li > a');
	// asideDepth2.forEach(depth2 => {
	// 	depth2.addEventListener('click', function (e) {
	// 		if(depth2.nextElementSibling) {
	// 			e.preventDefault();
	// 			if(depth2.classList.contains('on')) {
	// 				depth2.classList.remove('on');
	// 			} else {
	// 				asideDepth2.forEach(a => a.classList.remove('on'));
	// 				depth2.classList.add('on');
	// 			}
	// 		}
	// 	});
	// });

	const goTopButton = document.querySelector('.go-top');
	if (goTopButton) {
		goTopButton.addEventListener('click', () => {
			window.scrollTo({ top: 0, behavior: 'smooth' });
		});
	}
}


let loadSocket = {
	'header': false,
	'aside': false,
	'footer': false
}

window.addEventListener('DOMContentLoaded', () => {
	const includeTag = document.querySelectorAll('.js-include');
	if (includeTag.length === 0) {
		layoutEvent();
	} else {
		includeTag.forEach((divBox, idx) => {
			fetch(divBox.dataset.include)
				.then(res => res.text())
				.then(function (data) {
					divBox.innerHTML = data;
					loadSocket[divBox.dataset.event] = true;

					if (loadSocket.header && loadSocket.aside && loadSocket.footer) {
						layoutEvent();
					}
				});
		});
	}
});

