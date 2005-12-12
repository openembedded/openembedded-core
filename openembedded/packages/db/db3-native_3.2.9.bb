SECTION = "libs"
VIRTUAL_NAME = "virtual/db-native"
CONFLICTS = "db-native"
inherit native
include db3_${PV}.bb

FILESPATH = "${@base_set_filespath([ '${FILE_DIRNAME}/db3-${PV}', '${FILE_DIRNAME}/db3', '${FILE_DIRNAME}/files', '${FILE_DIRNAME}' ], d)}"
PACKAGES = ""
