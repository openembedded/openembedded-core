DEPENDS += "python-scons-native"

scons_do_compile() {
        ${STAGING_BINDIR_NATIVE}/scons PREFIX=${prefix} prefix=${prefix} || \
        bbfatal "scons build execution failed."
}

scons_do_install() {
        ${STAGING_BINDIR_NATIVE}/scons PREFIX=${D}${prefix} prefix=${D}${prefix} install || \
        bbfatal "scons install execution failed."
}

EXPORT_FUNCTIONS do_compile do_install
