SUMMARY = "Default icon theme that all icon themes automatically inherit from"
HOMEPAGE = "http://icon-theme.freedesktop.org/wiki/HicolorTheme"
BUGTRACKER = "https://bugs.freedesktop.org/"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=f08a446809913fc9b3c718f0eaea0426"

SECTION = "unknown"
inherit gnomebase allarch autotools-brokensep

PR = "r1"

SRC_URI = "http://icon-theme.freedesktop.org/releases/${BPN}-${PV}.tar.gz \
        file://index.theme"

SRC_URI[md5sum] = "21d0f50aa6b8eef02846cda9e5e9324c"
SRC_URI[sha256sum] = "a38b038915480d1ddd4e3c421562560a14d42ace0449a5acc07c50f57f9c3406"

FILES_${PN} += "${datadir}/icons"

do_install_append () {
	install -m 0644 ${WORKDIR}/index.theme ${D}/${datadir}/icons/hicolor
}
