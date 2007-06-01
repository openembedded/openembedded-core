require uboot-gta01_svn.bb

PROVIDES = ""
TARGET_LDFLAGS = ""

do_compile () {
	chmod +x board/neo1973/gta01/split_by_variant.sh
	oe_runmake gta01bv3_config
	oe_runmake clean
	oe_runmake tools
}

do_deploy () {
	install -m 0755 tools/mkimage ${STAGING_BINDIR_NATIVE}/uboot-mkimage
}

do_deploy[dirs] = "${S}"
addtask deploy before do_package after do_install
