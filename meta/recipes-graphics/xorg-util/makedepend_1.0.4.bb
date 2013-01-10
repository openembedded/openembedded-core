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
PR = "r1"
PE = "1"

BBCLASSEXTEND = "native"

LIC_FILES_CHKSUM = "file://COPYING;md5=43a6eda34b48ee821b3b66f4f753ce4f"

SRC_URI += "file://obsolete_automake_macros.patch"

SRC_URI[md5sum] = "bf1c54028427829c9c3193bef710dbee"
SRC_URI[sha256sum] = "f53b8ce5dec02a05f8994036a8ebac485a96324143ff2382e29578fdc096b04f"
