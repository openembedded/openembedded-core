SUMMARY = "Multiplexing terminal manager"
DESCRIPTION = "Screen is a full-screen window manager \
that multiplexes a physical terminal between several \
processes, typically interactive shells."
HOMEPAGE = "http://www.gnu.org/software/screen/"
BUGTRACKER = "https://savannah.gnu.org/bugs/?func=additem&group=screen"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=0774d66808b0f602e94448108f59448b \
                    file://screen.h;endline=23;md5=9a7ae69a2aafed891bf7c38ddf9f6b7d"

SECTION = "console/utils"
DEPENDS = "ncurses \
          ${@bb.utils.contains('DISTRO_FEATURES', 'pam', 'libpam', '', d)}"
RDEPENDS_${PN} = "base-files"

PR = "r4"

SRC_URI = "${GNU_MIRROR}/screen/screen-${PV}.tar.gz;name=tarball \
           ${DEBIAN_MIRROR}/main/s/screen/screen_4.0.3-14.diff.gz;name=patch \
           file://configure.patch \
           file://fix-parallel-make.patch \
           file://screen-4.0.3-CVE-2009-1214.patch \
           file://screen-4.0.2-CVE-2009-1215.patch \
           ${@bb.utils.contains('DISTRO_FEATURES', 'pam', '${PAM_SRC_URI}', '', d)}"

PAM_SRC_URI = "file://screen.pam"

SRC_URI[tarball.md5sum] = "8506fd205028a96c741e4037de6e3c42"
SRC_URI[tarball.sha256sum] = "78f0d5b1496084a5902586304d4a73954b2bfe33ea13edceecf21615c39e6c77"

SRC_URI[patch.md5sum] = "5960bdae6782ee9356b7e0e0a1fa7c19"
SRC_URI[patch.sha256sum] = "10acb274b2fb0bb7137a0d66e52fa0f18125bc5198c7a8d5af381b4b30636316"

inherit autotools texinfo

EXTRA_OECONF = "--with-pty-mode=0620 --with-pty-group=5 \
               ${@bb.utils.contains('DISTRO_FEATURES', 'pam', '--enable-pam', '--disable-pam', d)}"

do_install_append () {
	if [ "${@bb.utils.contains('DISTRO_FEATURES', 'pam', 'pam', '', d)}" = "pam" ]; then
		install -D -m 644 ${WORKDIR}/screen.pam ${D}/${sysconfdir}/pam.d/screen
	fi
}

pkg_postinst_${PN} () {
	grep -q "^${bindir}/screen$" $D${sysconfdir}/shells || echo ${bindir}/screen >> $D${sysconfdir}/shells
}

pkg_postrm_${PN} () {
	printf "$(grep -v "^${bindir}/screen$" $D${sysconfdir}/shells)\n" > $D${sysconfdir}/shells
}
