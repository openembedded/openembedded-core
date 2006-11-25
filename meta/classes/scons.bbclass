DEPENDS += "python-scons-native"

scons_do_compile() {
        ${STAGING_BINDIR_NATIVE}/scons || \
        oefatal "scons build execution failed."
}

scons_do_install() {
        ${STAGING_BINDIR_NATIVE}/scons install || \
        oefatal "scons install execution failed."
}

EXPORT_FUNCTIONS do_compile do_install
