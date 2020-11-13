inherit python3native

DEPENDS_append = " python3"

do_configure_prepend() {
        export _PYTHON_SYSCONFIGDATA_NAME="_sysconfigdata"
}

do_compile_prepend() {
        export _PYTHON_SYSCONFIGDATA_NAME="_sysconfigdata"
}

do_install_prepend() {
        export _PYTHON_SYSCONFIGDATA_NAME="_sysconfigdata"
}
