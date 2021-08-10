require rust-target.inc
require rust-source-${PV}.inc
require rust-snapshot-${PV}.inc

do_compile () {
    rust_runx build --stage 2
}

rust_do_install() {
    rust_runx install
}
