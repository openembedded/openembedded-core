require rust-target.inc
require rust-source-${PV}.inc
require rust-snapshot-${PV}.inc

INSANE_SKIP:${PN}:class-native = "already-stripped"

do_compile () {
    rust_runx build --stage 2
}

rust_do_install() {
    rust_runx install
}
