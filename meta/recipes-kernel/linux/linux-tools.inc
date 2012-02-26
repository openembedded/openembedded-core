# included by kernel recipes if they want to build/provide
# perf functionality from their tree.

BUILDPERF = "yes"
BUILDPERF_libc-uclibc = "no"
# perf requires binutils which is GPLv3 licensed, don't prevent the entire kernel
# being built if GPLv3 is in INCOMPATIBLE_LICENSE
python () {
    if ((d.getVar("INCOMPATIBLE_LICENSE", True) or "").find("GPLv3") != -1):
       # GPLv3, drop perf
       d.setVar("BUILDPERF", "no")
       d.setVar("PERFDEPENDS", "")
}

do_compile_perf () {
    if [ "${BUILDPERF}" = "yes" ]; then
	oe_runmake -C ${S}/tools/perf CC="${CC}" LD="${LD}" prefix=${prefix} NO_NEWT=1 NO_DWARF=1
    fi
}

fakeroot do_install_perf () {
    if [ "${BUILDPERF}" = "yes" ]; then
	oe_runmake -C ${S}/tools/perf CC="${CC}" LD="${LD}" prefix=${prefix} DESTDIR=${D} install NO_NEWT=1 NO_DWARF=1
    fi
}


# perf tasks
addtask compile_perf after do_compile before do_install
addtask install_perf after do_install before do_package

do_compile_perf[umask] = "022"
do_install_perf[umask] = "022"

PERFDEPENDS = "virtual/${MLPREFIX}libc:do_populate_sysroot ${MLPREFIX}elfutils:do_populate_sysroot ${MLPREFIX}binutils:do_populate_sysroot"
PERFDEPENDS_libc-uclibc = ""
PERFRDEPENDS = "python perl elfutils"
PERFRDEPENDS_libc-uclibc = ""

do_compile_perf[depends] = "${PERFDEPENDS}"
RDEPENDS_perf += "${PERFRDEPENDS}"
