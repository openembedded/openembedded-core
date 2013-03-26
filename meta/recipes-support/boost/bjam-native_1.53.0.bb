include boost-${PV}.inc

DESCRIPTION = "Portable Boost.Jam build tool for boost"
SECTION = "devel"

inherit native

do_compile() {
    ./bootstrap.sh --with-toolset=gcc
}

do_install() {
    install -d ${D}${bindir}/
    install -c -m 755 bjam ${D}${bindir}/
}
