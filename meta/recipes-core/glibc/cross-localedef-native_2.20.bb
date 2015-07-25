SUMMARY = "Cross locale generation tool for glibc"
HOMEPAGE = "http://www.gnu.org/software/libc/libc.html"
SECTION = "libs"
LICENSE = "LGPL-2.1"

LIC_FILES_CHKSUM = "file://LICENSES;md5=e9a558e243b36d3209f380deb394b213 \
      file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
      file://posix/rxspencer/COPYRIGHT;md5=dc5485bb394a13b2332ec1c785f5d83a \
      file://COPYING.LIB;md5=4fbd65380cdd255951079008b364516c"


inherit native
inherit autotools

FILESEXTRAPATHS =. "${FILE_DIRNAME}/${PN}:${FILE_DIRNAME}/glibc:"

PV = "2.20"

SRC_URI = "git://sourceware.org/git/glibc.git;branch=release/${PV}/master;name=glibc \
           git://github.com/kraj/localedef;branch=master;name=localedef;destsuffix=git/localedef \
	   file://fix_for_centos_5.8.patch \
           ${EGLIBCPATCHES} \
          "
EGLIBCPATCHES = "\
           file://timezone-re-written-tzselect-as-posix-sh.patch \
           file://eglibc.patch \
           file://option-groups.patch \
           file://GLRO_dl_debug_mask.patch \
           file://eglibc-header-bootstrap.patch \
           file://eglibc-resolv-dynamic.patch \
           file://eglibc-ppc8xx-cache-line-workaround.patch \
           file://eglibc-sh4-fpscr_values.patch \
           file://eglibc-use-option-groups.patch \
          "

SRCREV_glibc = "b8079dd0d360648e4e8de48656c5c38972621072"
SRCREV_localedef = "c833367348d39dad7ba018990bfdaffaec8e9ed3"

S = "${WORKDIR}/git"

EXTRA_OECONF = "--with-glibc=${S}"
CFLAGS += "-fgnu89-inline -std=gnu99 -DNOT_IN_libc=1"

do_configure () {
	${S}/localedef/configure ${EXTRA_OECONF}
}


do_install() {
	install -d ${D}${bindir} 
	install -m 0755 ${B}/localedef ${D}${bindir}/cross-localedef
}
