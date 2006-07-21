SECTION = "libs"
include zlib_${PV}.bb
inherit native
DEPENDS = ""
FILESPATH = "${@base_set_filespath([ '${FILE_DIRNAME}/zlib-${PV}', '${FILE_DIRNAME}/zlib', '${FILE_DIRNAME}/files', '${FILE_DIRNAME}' ], d)}"
