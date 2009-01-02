require libxml2.inc

PR = "r8"

DEPENDS += "python-native"

EXTRA_OECONF = "--with-python=${STAGING_DIR_NATIVE}/${prefix} --without-debug --without-legacy --with-catalog --without-docbook --with-c14n"

inherit native

do_stage () {
	oe_runmake install
}
