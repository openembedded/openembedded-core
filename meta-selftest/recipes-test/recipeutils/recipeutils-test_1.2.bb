SUMMARY = "Test recipe for recipeutils.patch_recipe()"

require recipeutils-test.inc

LICENSE = "Proprietary"

DEPENDS += "virtual/libx11"

BBCLASSEXTEND = "native nativesdk"

SRC_URI += "file://somefile"

SRC_URI_append = " file://anotherfile"
