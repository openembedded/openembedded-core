require xorg-util-common.inc

SUMMARY = "create dependencies in makefiles"

DESCRIPTION = "The makedepend program reads each sourcefile in sequence \
and parses it like a C-preprocessor, processing \
all #include, #define,  #undef, #ifdef, #ifndef, #endif, #if, #elif \
and #else directives so that it can correctly tell which #include, \
directives would be used in a compilation. Any #include, directives \
can reference files having other #include directives, and parsing will \
occur in these files as well."

DEPENDS = "xproto util-macros"
PR = "r0"
PE = "1"

BBCLASSEXTEND = "native"

LIC_FILES_CHKSUM = "file://COPYING;md5=43a6eda34b48ee821b3b66f4f753ce4f"

SRC_URI[md5sum] = "ec37ca8b810a40cdfb16a736b3360f6c"
SRC_URI[sha256sum] = "2d3466acc29b382a4368b30371f17a4083933281b97f8ef67fc8b785a60d52dc"

