DEPENDS += "python-scons-native"

scons_do_compile() {
        ${STAGING_BINDIR}/scons || \
        oefatal "scons build execution failed."
}

scons_do_install() {
        ${STAGING_BINDIR}/scons install || \
        oefatal "scons install execution failed."
}

EXPORT_FUNCTIONS do_compile do_install
