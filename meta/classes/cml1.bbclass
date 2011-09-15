cml1_do_configure() {
	set -e
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
	oe_runmake oldconfig
}

EXPORT_FUNCTIONS do_configure
addtask configure after do_unpack do_patch before do_compile

inherit terminal

python do_menuconfig() {
        oe_terminal("make menuconfig", '${PN} Configuration', d)
}
do_menuconfig[nostamp] = "1"
addtask menuconfig after do_configure

