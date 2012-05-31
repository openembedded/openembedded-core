cml1_do_configure() {
	set -e
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
	oe_runmake oldconfig
}

EXPORT_FUNCTIONS do_configure
addtask configure after do_unpack do_patch before do_compile

inherit terminal

OE_TERMINAL_EXPORTS += "HOST_EXTRACFLAGS HOSTLDFLAGS HOST_LOADLIBES"
HOST_EXTRACFLAGS = "${BUILD_CFLAGS} ${BUILD_LDFLAGS}"
HOSTLDFLAGS = "${BUILD_LDFLAGS}"
HOST_LOADLIBES = "-lncurses"

python do_menuconfig() {
        oe_terminal("${SHELL} -c \"make menuconfig; echo 'Pausing for 5 seconds'; sleep 5\"", '${PN} Configuration', d)
}
do_menuconfig[depends] += "ncurses-native:do_populate_sysroot"
do_menuconfig[nostamp] = "1"
addtask menuconfig after do_configure

