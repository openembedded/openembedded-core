require gcc-${PV}.inc
require gcc-configure-target.inc
require gcc-package-target.inc

ARCH_FLAGS_FOR_TARGET += "-isystem${STAGING_INCDIR}"
