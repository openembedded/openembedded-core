inherit native

require pax-utils_${PV}.bb

do_stage() {
    oe_runmake PREFIX=${STAGING_DIR_HOST}${layout_prefix} install
}
