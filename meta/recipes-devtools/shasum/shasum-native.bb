require shasum.inc

inherit native

INHIBIT_DEFAULT_DEPS = "1"
PATCHTOOL = "patch"

do_fetch[depends] = ""
