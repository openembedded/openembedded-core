require gcc-${PV}.inc
require gcc-configure-runtime.inc
require gcc-package-runtime.inc

ARCH_FLAGS_FOR_TARGET += "-isystem${STAGING_INCDIR}"

EXTRA_OECONF += "--disable-libunwind-exceptions"
EXTRA_OECONF_append_linuxstdbase = " --enable-clocale=gnu"
