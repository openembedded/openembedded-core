BASE_PACKAGE_ARCH = "core2"
TARGET_CC_ARCH = "-m32 -march=core2 -msse3 -mtune=generic -mfpmath=sse"
#MOBLIN_CFLAGS = "-Os -Wp,-D_FORTIFY_SOURCE=2 -fexceptions -fstack-protector --param=ssp-buffer-size=4 -fasynchronous-unwind-tables"

PACKAGE_EXTRA_ARCHS += "x86 i386 i486 core2"

# Work around a gcc bug for core2 which caused matchbox-panel to segfault
# and possibly others.
FULL_OPTIMIZATION_pn-gtk+ = "-fexpensive-optimizations -frename-registers -O2 -ggdb -feliminate-unused-debug-types"
