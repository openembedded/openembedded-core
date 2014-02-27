SUMMARY = "Tool for working with series of patches"
HOMEPAGE = "http://savannah.nongnu.org/projects/quilt/"
SECTION = "devel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

SRC_URI = "http://download.savannah.gnu.org/releases/quilt/quilt-${PV}.tar.gz \
        file://install.patch \
        file://run-ptest \
        file://Makefile \
"

SRC_URI[md5sum] = "24417eba5961592b64103bdda001ebf4"
SRC_URI[sha256sum] = "1eec0a270ac4d41eea0fd5823603c9d2a35ab6b8ab73a90dd92ce1291b2a9fc3"

inherit autotools-brokensep ptest

EXTRA_OECONF_darwin += "--without-date \
                        --without-getopt \
                        "

PACKAGES += "guards guards-doc"
FILES_${PN} = "${sysconfdir} ${datadir}/quilt \
               ${bindir}/quilt ${libdir}/quilt"
FILES_guards = "${bindir}/guards"
FILES_${PN}-doc = "${mandir}/man1/quilt.1 ${docdir}/${BPN}"
FILES_guards-doc = "${mandir}/man1/guards.1"

RDEPENDS_${PN} = "bash"

EXTRA_OE_MAKE_ARGS_darwin ?= ""
EXTRA_OE_MAKE_ARGS ?= "BUILD_ROOT=${D}"

CACHED_CONFIGUREVARS += "ac_cv_path_BASH=/bin/bash"

# quilt ignores DESTDIR
do_install () {
	oe_runmake ${EXTRA_OE_MAKE_ARGS} install
	# cleanup unpackaged files
	rm -rf ${D}/${datadir}/emacs
}

do_compile_ptest() {
	oe_runmake bin/patch-wrapper test/.depend
}

do_install_ptest() {
	tar -cf - bin/ --exclude \*.in | ( cd ${D}${PTEST_PATH} && tar -xf - )
	tar -cf - compat/ --exclude \*.in | ( cd ${D}${PTEST_PATH} && tar -xf - )
	tar -cf - quilt/ --exclude \*.in | ( cd ${D}${PTEST_PATH} && tar -xf - )
	tar -cf - test/ --exclude mail.test --exclude delete.test | ( cd ${D}${PTEST_PATH} && tar -xf - )
	cp ${WORKDIR}/Makefile ${D}${PTEST_PATH}
}

RDEPENDS_${PN}-ptest = "make file sed gawk diffutils findutils ed perl \
                        perl-module-filehandle perl-module-getopt-std \
                        perl-module-posix perl-module-file-temp \
"
