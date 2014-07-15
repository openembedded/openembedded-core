SUMMARY = "Displays the full path of shell commands"
DESCRIPTION = "Which is a utility that prints out the full path of the \
executables that bash(1) would execute when the passed \
program names would have been entered on the shell prompt. \
It does this by using the exact same algorithm as bash."
SECTION = "libs"
HOMEPAGE = "http://carlo17.home.xs4all.nl/which/"

LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504\
                    file://which.c;beginline=1;endline=17;md5=a9963693af2272e7a8df6f231164e7a2"
DEPENDS     = "cwautomacros-native"

inherit autotools texinfo update-alternatives

PR = "r3"

EXTRA_OECONF = "--disable-iberty"

SRC_URI = "${GNU_MIRROR}/which/which-${PV}.tar.gz \
           file://automake.patch \
           file://remove-declaration.patch"

SRC_URI[md5sum] = "95be0501a466e515422cde4af46b2744"
SRC_URI[sha256sum] = "d417b65c650d88ad26a208293c1c6e3eb60d4b6d847f01ff8f66aca63e2857f8"

do_configure_prepend() {
	sed -i -e 's%@ACLOCAL_CWFLAGS@%-I ${STAGING_DIR_NATIVE}/usr/share/cwautomacros/m4%g' ${S}/Makefile.am ${S}/tilde/Makefile.am
}

ALTERNATIVE_${PN} = "which"
ALTERNATIVE_PRIORITY = "100"
