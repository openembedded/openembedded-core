# Path mangling needed by the cross packaging
# Note that we use := here, and require that this is included at the correct 
# point (before inheriting cross) to ensure that libdir and includedir are
# target paths, not CROSS_DIR paths.
target_libdir := "${libdir}"
target_includedir := "${includedir}"
target_base_libdir := "${base_libdir}"
target_prefix := "${prefix}"

inherit cross

DEPENDS = "virtual/${TARGET_PREFIX}binutils virtual/${TARGET_PREFIX}libc-for-gcc"
PROVIDES = "virtual/${TARGET_PREFIX}gcc virtual/${TARGET_PREFIX}g++"
