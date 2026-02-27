SUMMARY = "Autologin package for the Weston Wayland compositor"
HOMEPAGE = "https://www.yoctoproject.org/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "\
           file://weston.ini \
           file://emptty.conf \
"

S = "${UNPACKDIR}"

PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'xwayland', '', d)}"
PACKAGECONFIG:append:qemuriscv64 = " use-pixman"
PACKAGECONFIG:append:qemuppc64 = " use-pixman"

PACKAGECONFIG[xwayland] = ",,"
PACKAGECONFIG[no-idle-timeout] = ",,"
PACKAGECONFIG[use-pixman] = ",,"

DEFAULTBACKEND ??= ""
DEFAULTBACKEND:qemuall ?= "drm"
WESTON_USER ??= "weston"
WESTON_USER_HOME ??= "/home/${WESTON_USER}"

do_install() {
	install -D -p -m0644 ${S}/weston.ini ${D}${sysconfdir}/xdg/weston/weston.ini
	install -D -p -m0644 ${S}/emptty.conf ${D}${sysconfdir}/emptty/conf

	if [ -n "${DEFAULTBACKEND}" ]; then
		sed -i -e "/^\[core\]/a backend=${DEFAULTBACKEND}-backend.so" ${D}${sysconfdir}/xdg/weston/weston.ini
	fi

	if [ "${@bb.utils.contains('PACKAGECONFIG', 'xwayland', 'yes', 'no', d)}" = "yes" ]; then
		sed -i -e "/^\[core\]/a xwayland=true" ${D}${sysconfdir}/xdg/weston/weston.ini
	fi

	if [ "${@bb.utils.contains('PACKAGECONFIG', 'no-idle-timeout', 'yes', 'no', d)}" = "yes" ]; then
		sed -i -e "/^\[core\]/a idle-time=0" ${D}${sysconfdir}/xdg/weston/weston.ini
	fi

	if [ "${@bb.utils.contains('PACKAGECONFIG', 'use-pixman', 'yes', 'no', d)}" = "yes" ]; then
		sed -i -e "/^\[core\]/a use-pixman=true" ${D}${sysconfdir}/xdg/weston/weston.ini
	fi

	install -dm 755 -o ${WESTON_USER} -g ${WESTON_USER} ${D}/${WESTON_USER_HOME}
}

inherit useradd

USERADD_PACKAGES = "${PN}"

# rdepends on weston which depends on virtual/egl
#
require ${THISDIR}/required-distro-features.inc

RDEPENDS:${PN} = "emptty weston kbd ${@bb.utils.contains('PACKAGECONFIG', 'xwayland', 'weston-xwayland', '', d)}"

FILES:${PN} += "\
    ${sysconfdir}/xdg/weston/weston.ini \
    ${sysconfdir}/emptty/conf \
    ${WESTON_USER_HOME} \
    "

CONFFILES:${PN} += "${sysconfdir}/xdg/weston/weston.ini ${sysconfdir}/emptty/conf"
RPROVIDES:${PN} += "virtual-emptty-conf"

USERADD_PARAM:${PN} = "--home ${WESTON_USER_HOME} --shell /bin/sh --user-group -G video,input,render,seat,nopasswdlogin weston"
GROUPADD_PARAM:${PN} = "-r nopasswdlogin; -r render; -r seat"
