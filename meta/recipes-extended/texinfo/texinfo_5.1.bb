SUMMARY = "Documentation system for on-line information and printed output"
DESCRIPTION = "Texinfo is a documentation system that can produce both \
online information and printed output from a single source file. The \
GNU Project uses the Texinfo file format for most of its documentation."
HOMEPAGE = "http://www.gnu.org/software/texinfo/"
SECTION = "console/utils"
LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

DEPENDS = "zlib ncurses texinfo-native"
DEPENDS_class-native = "zlib-native ncurses-native"

TARGET_PATCH = "file://use_host_makedoc.patch"
TARGET_PATCH_class-native = ""

SRC_URI = "${GNU_MIRROR}/texinfo/${BP}.tar.gz \
           file://texinfo-4.12-zlib.patch \
           file://texinfo-4.13a-powerpc.patch \
           file://disable-native-tools.patch \
           file://link-zip.patch \
           file://dont-depend-on-help2man.patch \
           file://enumerate_greater_than_ten.patch \
           ${TARGET_PATCH} \
          "

SRC_URI[md5sum] = "54e250014fe698fb4832016158747c03"
SRC_URI[sha256sum] = "50e8067f9758bb2bf175b69600082ac4a27c464cb4bcd48a578edd3127216600"

S = "${WORKDIR}/${BP}"
tex_texinfo = "texmf/tex/texinfo"

inherit gettext autotools

do_compile_prepend() {
	if [ -d tools ];then
		oe_runmake -C tools/gnulib/lib
	fi
}

do_install_append() {
	mkdir -p ${D}${datadir}/${tex_texinfo}
	install -p -m644 ${S}/doc/texinfo.tex ${S}/doc/txi-??.tex ${D}${datadir}/${tex_texinfo} 	
	sed -i -e '1s,#!.*perl,#! ${USRBINPATH}/env perl,' ${D}${bindir}/texi2any ${D}${bindir}/pod2texi
}

do_install_append_class-native() {
	install -m 755 info/makedoc ${D}${bindir}
}

PACKAGES += "info info-doc"

FILES_info = "${bindir}/info ${bindir}/infokey ${bindir}/install-info"
FILES_info-doc = "${infodir}/info.info ${infodir}/dir ${infodir}/info-*.info \
                  ${mandir}/man1/info.1* ${mandir}/man5/info.5* \
                  ${mandir}/man1/infokey.1* ${mandir}/man1/install-info.1*"

FILES_${PN} = "${bindir}/makeinfo ${bindir}/texi* ${bindir}/pdftexi2dvi ${bindir}/pod2texi ${datadir}/texinfo"
RDEPENDS_${PN} = "perl"
FILES_${PN}-doc = "${infodir}/texinfo* \
                   ${datadir}/${tex_texinfo} \
                   ${mandir}/man1 ${mandir}/man5"

BBCLASSEXTEND = "native"
