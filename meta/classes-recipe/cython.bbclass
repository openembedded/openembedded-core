DEPENDS:append = " python3-cython-native"

# Remap the build paths that appear in generated .c code
export CYTHON_PREFIX_MAP = "${S}=${TARGET_DBGSRC_DIR} ${B}=${TARGET_DBGSRC_DIR}"

do_compile[postfuncs] = "strip_cython_metadata"
strip_cython_metadata() {
    # Remove the Cython Metadata headers that we don't need after the build, and
    # may contain build paths.
    find ${S} -name "*.c" -print0 | xargs -0 sed -i -e "/BEGIN: Cython Metadata/,/END: Cython Metadata/d"
}
