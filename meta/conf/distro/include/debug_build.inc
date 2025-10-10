# Override SELECTED_OPTIMIZATION and BUILD_OPTIMIZATION when DEBUG_BUILD is enabled.
DEBUG_OPTIMIZATION ?= "-Og ${DEBUG_LEVELFLAG}"
SELECTED_OPTIMIZATION ?= "${DEBUG_OPTIMIZATION}"
# compiler flags for native/nativesdk
BUILD_OPTIMIZATION ?= "-Og -g"

# The modern compilers and code seem to require extra steps to avoid DEBUG errors,
# this file collects debug tuning configuration to address DEBUG errors.

DEBUG_OPTIMIZATION:append:pn-perf = " -Wno-error=maybe-uninitialized"
DEBUG_OPTIMIZATION:append:armv4:pn-libjpeg-turbo = " ${@bb.utils.contains('TUNE_CCARGS', '-mthumb', '-fomit-frame-pointer', '', d)}"
DEBUG_OPTIMIZATION:append:armv5:pn-libjpeg-turbo = " ${@bb.utils.contains('TUNE_CCARGS', '-mthumb', '-fomit-frame-pointer', '', d)}"
DEBUG_OPTIMIZATION:append:armv4:pn-bash = " ${@bb.utils.contains('TUNE_CCARGS', '-mthumb', '-fomit-frame-pointer', '', d)}"
DEBUG_OPTIMIZATION:append:armv5:pn-bash = " ${@bb.utils.contains('TUNE_CCARGS', '-mthumb', '-fomit-frame-pointer', '', d)}"
DEBUG_OPTIMIZATION:append:pn-mdadm = " -Wno-error"
DEBUG_OPTIMIZATION:mips:pn-kea = "-O ${DEBUG_LEVELFLAG}"
DEBUG_OPTIMIZATION:mipsel:pn-kea = "-O ${DEBUG_LEVELFLAG}"
# {standard input}: Assembler messages:
# {standard input}:1488805: Error: branch out of range
DEBUG_OPTIMIZATION:mips:pn-python3-lxml = "-O ${DEBUG_LEVELFLAG}"
DEBUG_OPTIMIZATION:mipsel:pn-python3-lxml = "-O ${DEBUG_LEVELFLAG}"
# used to fix ../../../../../../../../../work-shared/gcc-8.3.0-r0/gcc-8.3.0/libsanitizer/libbacktrace/../../libbacktrace/elf.c:772:21: error: 'st.st_mode' may be used uninitialized in this function [-Werror=maybe-uninitialized]
DEBUG_OPTIMIZATION:append:pn-gcc-sanitizers = " -Wno-error"

BUILD_OPTIMIZATION:mips:pn-kea = "-O -g"
BUILD_OPTIMIZATION:mipsel:pn-kea = "-O -g"
# {standard input}: Assembler messages:
# {standard input}:1488805: Error: branch out of range
BUILD_OPTIMIZATION:mips:pn-python3-lxml = "-O -g"
BUILD_OPTIMIZATION:mipsel:pn-python3-lxml = "-O -g"

CPPFLAGS:append:arm:pn-lttng-ust = " -DUATOMIC_NO_LINK_ERROR"

OECMAKE_WEBKIT_NO_INLINE_HINTS:pn-webkitgtk = "-DWEBKIT_NO_INLINE_HINTS=ON"

EXTRA_OECONF:append:pn-debugedit = " --disable-inlined-xxhash"
EXTRA_OECONF:append:pn-debugedit-native = " --disable-inlined-xxhash"
EXTRA_OECONF:append:pn-nativesdk-debugedit = " --disable-inlined-xxhash"

lcl_maybe_fortify:pn-vim = ""

CXXFLAGS:append:pn-vulkan-validation-layers = " -DXXH_NO_INLINE_HINTS=1"

# No strip sysroot for cross and native
INHIBIT_SYSROOT_STRIP:class-cross ?= "1"
INHIBIT_SYSROOT_STRIP:class-native ?= "1"

# For meson.bbclass
MESON_BUILDTYPE ?= "debug"

# For cargo.bbclass
BUILD_MODE ?= ""
BUILD_DIR ?= "debug"
