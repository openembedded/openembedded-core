# Note that we use := here, and require that this
# is included at the correct point (before inheriting
# cross) to ensure that libdir and includedir are
# target paths, not CROSS_DIR paths.
target_libdir := "${libdir}"
target_includedir := "${includedir}"
target_base_libdir := "${base_libdir}"
