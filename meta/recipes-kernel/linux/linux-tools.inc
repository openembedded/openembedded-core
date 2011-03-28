# included by kernel recipes if they want to build/provide
# perf functionality from their tree.

do_compile_perf() {
	oe_runmake -C ${S}/tools/perf CC="${CC}" LD="${LD}" prefix=${prefix}
}

do_install_perf() {
	oe_runmake -C ${S}/tools/perf CC="${CC}" LD="${LD}" prefix=${prefix} DESTDIR=${D} install
}


# perf tasks
addtask compile_perf after do_compile before do_install
addtask install_perf after do_install before do_package

do_compile_perf[depends] =  "virtual/libc:do_populate_sysroot"
do_compile_perf[depends] =+ "elfutils:do_populate_sysroot"
RDEPENDS_perf += "python perl elfutils"
