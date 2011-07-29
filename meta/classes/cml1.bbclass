cml1_do_configure() {
	set -e
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
	oe_runmake oldconfig
}

EXPORT_FUNCTIONS do_configure
addtask configure after do_unpack do_patch before do_compile

do_menuconfig() {
	export TERMWINDOWTITLE="${PN} Configuration"
	export SHELLCMDS="make menuconfig"
	${TERMCMDRUN}
	if [ $? -ne 0 ]; then
		oefatal "'${TERMCMD}' not found. Check TERMCMD variable."
	fi
}
do_menuconfig[nostamp] = "1"
addtask menuconfig after do_configure

