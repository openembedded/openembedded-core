DESCRIPTION = "File attempts to classify files depending \
on their contents and prints a description if a match is found."
SECTION = "console/utils"
LICENSE = "BSD-ADV"
DEPENDS = "file-native"

SRC_URI = "ftp://ftp.astron.com/pub/file/file-${PV}.tar.gz"
S = "${WORKDIR}/file-${PV}"

inherit autotools 

do_configure_prepend() {
	sed -i -e 's,$(top_builddir)/src/file,file,' ${S}/magic/Makefile.am
}

do_stage() {
	autotools_stage_all
}
