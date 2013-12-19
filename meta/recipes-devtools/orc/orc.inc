SUMMARY = "Optimised Inner Loop Runtime Compiler"
HOMEPAGE = "http://code.entropywave.com/projects/orc/"
LICENSE = "BSD-2-Clause & BSD-3-Clause"
LIC_FILES_CHKSUM = "file://COPYING;md5=1400bd9d09e8af56b9ec982b3d85797e"

INC_PR = "r2"

SRC_URI = "http://code.entropywave.com/download/orc/orc-${PV}.tar.gz;name=orc"

inherit autotools pkgconfig

BBCLASSEXTEND = "native nativesdk"

PACKAGES =+ "orc-examples"
FILES_orc-examples = "${libdir}/orc/*"
FILES_${PN} = "${bindir}/*"

python populate_packages_prepend () {
    libdir = d.expand('${libdir}')
    do_split_packages(d, libdir, '^lib(.*)\.so\.*', 'lib%s', 'ORC %s library', extra_depends='', allow_links=True)
}

do_compile_prepend_virtclass-native () {
    sed -i -e 's#/tmp#.#g' ${S}/orc/orccodemem.c
}
