PR = "r5"

require gcc-${PV}.inc
require gcc-configure-runtime.inc
require gcc-package-runtime.inc

SRC_URI_append = "file://fortran-cross-compile-hack.patch"

ARCH_FLAGS_FOR_TARGET += "-isystem${STAGING_INCDIR}"

EXTRA_OECONF += "--disable-libunwind-exceptions"

# gcc 4.5 ends up in ICE when using -feliminate-dwarf2-dups in libstdc++
# compiling pre compiled headers so we remove this option when compiling
# to workaround it
CXXFLAGS := "${@oe_filter_out('-feliminate-dwarf2-dups', '${CXXFLAGS}', d)}"
