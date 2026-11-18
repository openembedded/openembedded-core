SUMMARY = "A library for faking the system time in user-space programs"
SECTION = "libs"
HOMEPAGE = "https://github.com/wolfcw/libfaketime"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRCREV = "c88b79b4e9cc1c94294037b4677b69185861c2a1"

SRC_URI = "git://github.com/wolfcw/libfaketime.git;branch=master;tag=v${PV};protocol=https \
           file://0001-Mark-write-only-real_-pointers-as-unused.patch \
           "

# The version script names the symbols that are defined with .symver
LDFLAGS += "${@bb.utils.contains('DISTRO_FEATURES', 'ld-is-lld', ' -Wl,--undefined-version', '', d)}"

do_install () {
    install -d ${D}${libdir}/faketime
    oe_libinstall -C src libfaketime ${D}${libdir}/faketime

    install -d ${D}${bindir}
    install -m 0755 src/faketime ${D}${bindir}

    oe_runmake -C ${B}/man install DESTDIR=${D} PREFIX=${prefix}
}

FILES:${PN} = "${bindir}/faketime ${libdir}/faketime/lib*${SOLIBS}"
FILES:${PN}-dev += "${libdir}/faketime/lib*${SOLIBSDEV}"

BBCLASSEXTEND = "native"

# Building with -O2 results in clobbered variable error on 0.9.13
FULL_OPTIMIZATION = "-O1"
