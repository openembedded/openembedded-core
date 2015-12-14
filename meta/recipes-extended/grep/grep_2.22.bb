SUMMARY = "GNU grep utility"
HOMEPAGE = "http://savannah.gnu.org/projects/grep/"
BUGTRACKER = "http://savannah.gnu.org/bugs/?group=grep"
SECTION = "console/utils"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=8006d9c814277c1bfc4ca22af94b59ee"

SRC_URI = "${GNU_MIRROR}/grep/grep-${PV}.tar.xz \
           file://0001-Unset-need_charset_alias-when-building-for-musl.patch \
          "

SRC_URI[md5sum] = "e1015e951a49a82b02e38891026ef5df"
SRC_URI[sha256sum] = "ca91d22f017bfcb503d4bc3b44295491c89a33a3df0c3d8b8614f2d3831836eb"

inherit autotools gettext texinfo

EXTRA_OECONF = "--disable-perl-regexp"

do_configure_prepend () {
	rm -f ${S}/m4/init.m4
}

do_install () {
	autotools_do_install
	if [ "${base_bindir}" != "${bindir}" ]; then
		install -d ${D}${base_bindir}
		mv ${D}${bindir}/grep ${D}${base_bindir}/grep
		mv ${D}${bindir}/egrep ${D}${base_bindir}/egrep
		mv ${D}${bindir}/fgrep ${D}${base_bindir}/fgrep
		rmdir ${D}${bindir}/
	fi
}

inherit update-alternatives

ALTERNATIVE_PRIORITY = "100"

ALTERNATIVE_${PN} = "grep egrep fgrep"
ALTERNATIVE_LINK_NAME[grep] = "${base_bindir}/grep"
ALTERNATIVE_LINK_NAME[egrep] = "${base_bindir}/egrep"
ALTERNATIVE_LINK_NAME[fgrep] = "${base_bindir}/fgrep"

export CONFIG_SHELL="/bin/sh"
