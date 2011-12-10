SUMMARY = "Documentation system for on-line information and printed output"
DESCRIPTION = "Texinfo is a documentation system that can produce both \
online information and printed output from a single source file. The \
GNU Project uses the Texinfo file format for most of its documentation."
HOMEPAGE = "http://www.gnu.org/software/texinfo/"
SECTION = "console/utils"
LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=adefda309052235aa5d1e99ce7557010"
PR = "r2"

DEPENDS = "zlib ncurses texinfo-native"
DEPENDS_virtclass-native = "zlib-native ncurses-native"

TARGET_PATCH = "file://use_host_makedoc.patch"
TARGET_PATCH_virtclass-native = ""

SRC_URI = "${GNU_MIRROR}/texinfo/texinfo-${PV}.tar.gz \
           file://texinfo-4.12-zlib.patch \
           file://texinfo-4.13a-data_types.patch \
           file://texinfo-4.13a-mosdo-crash.patch \
           file://texinfo-4.13a-powerpc.patch \
           file://texinfo-4.13a-help-index-segfault.patch \
           file://disable-native-tools.patch \
           file://link-zip.patch \
           file://gettext-macros.patch \
           ${TARGET_PATCH}"

SRC_URI[md5sum] = "71ba711519209b5fb583fed2b3d86fcb"
SRC_URI[sha256sum] = "1303e91a1c752b69a32666a407e9fbdd6e936def4b09bc7de30f416301530d68"

S = ${WORKDIR}/texinfo-4.13
tex_texinfo = "texmf/tex/texinfo"

inherit gettext autotools

do_compile_prepend() {
	if [ -d tools ];then
		oe_runmake -C tools/gnulib/lib
	fi
}

do_install_append() {
	mkdir -p ${D}${datadir}/${tex_texinfo}
	install -p -m644 doc/texinfo.tex doc/txi-??.tex ${D}${datadir}/${tex_texinfo} 	
}
do_install_append_virtclass-native() {
	install -m 755 info/makedoc ${D}${bindir}
}

PACKAGES += "info info-doc"

FILES_info = "${bindir}/info ${bindir}/infokey ${bindir}/install-info"
FILES_info-doc = "${infodir}/info.info ${infodir}/dir ${infodir}/info-*.info \
                  ${mandir}/man1/info.1* ${mandir}/man5/info.5* \
                  ${mandir}/man1/infokey.1* ${mandir}/man1/install-info.1*"

FILES_${PN} = "${bindir}/makeinfo ${bindir}/texi* ${bindir}/pdftexi2dvi"
FILES_${PN}-doc = "${datadir}/texinfo ${infodir}/texinfo* \
                   ${datadir}/${tex_texinfo} \
                   ${mandir}/man1/makeinfo.1* ${mandir}/man5/texinfo.5* \
                   ${mandir}/man1/texindex.1* ${mandir}/man1/texi2dvi.1* \
                   ${mandir}/man1/texi2pdf.1* ${mandir}/man1/pdftexi2dvi.1*"

BBCLASSEXTEND = "native"
