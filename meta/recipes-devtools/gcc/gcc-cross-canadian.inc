inherit cross-canadian

DESCRIPTION = "The cross-canadian GNU cc and gcc C compilers for ${TARGET_ARCH} target."
PN = "gcc-cross-canadian-${TRANSLATED_TARGET_ARCH}"
BPN = "gcc"

DEPENDS = "virtual/${HOST_PREFIX}binutils-crosssdk virtual/nativesdk-${HOST_PREFIX}libc-for-gcc nativesdk-gettext"

