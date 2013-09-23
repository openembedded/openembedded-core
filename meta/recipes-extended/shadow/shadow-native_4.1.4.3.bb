require shadow.inc

PR = "r8"

SRC_URI += " \
           file://add_root_cmd_options.patch \
           file://disable-syslog.patch \
           file://useradd.patch \
           file://add_root_cmd_groupmems.patch \
           "
inherit native

EXTRA_OECONF += "--without-libpam \
                 --without-nscd"

