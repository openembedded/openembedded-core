require boost-${PV}.inc

SUMMARY = "Portable Boost.Jam build tool for boost"
SECTION = "devel"

inherit native

do_compile() {
    ./bootstrap.sh --with-toolset=gcc
}

do_install() {
    install -d ${D}${bindir}/
    install b2 ${D}${bindir}/bjam
}

# The build is either release mode (pre-stripped) or debug (-O0).
INSANE_SKIP_${PN} = "already-stripped"
