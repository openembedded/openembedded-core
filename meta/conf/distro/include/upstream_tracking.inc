# upstream_tracking.inc - This file contains data that tracks the
# upstream project associated with a given recipe. This list is needed
# for recipes that version information can not be automagically discovered.
# As we automate this checking, this file will slowed be reduced.
#
# This data is used by the package reporting system (packages.yoctoproject.org)
#
# The format is as a bitbake variable override for each recipe
#
#       RECIPE_UPSTREAM_VERSION_pn-<recipe name> = "<version>"
#       RECIPE_UPSTREAM_DATE_pn-<recipe name> = "Mmm dd, YYYY"
#	 - This is the latest upstream version and date
#       RECIPE_NO_UPDATE_REASON_pn-<recipe name> = "<free form text>"
#        - Used to note why a recipe should not be upgraded to a newer versoin
#       CHECK_DATE_pn-<recipe name> = "Mmm dd, YYYY"
#        - This is the date that upstream was last manually checked over time
#          this should be removed as we automate this checking
#
# These entries will slowly be removed as we can automatically determine
# the information in this file.  So for existing recipes that we can get 
# from the Recipe reporting system, the entries here can be removed.
#

# SVN support isn't implemented
# Also a new tarball release (or SVN tag) hasn't happened in a long time
RECIPE_UPSTREAM_VERSION_pn-unfs3="0.9.22.r495"
RECIPE_UPSTREAM_DATE_pn-unfs3="Feb 05, 2015"
CHECK_DATE_pn-unfs3 = "Jun 10, 2015"

# NO UPDATE REASONS
RECIPE_NO_UPDATE_REASON_pn-createrepo = "Versions after 0.9.* use YUM, so we hold at 0.4.11"
RECIPE_NO_UPDATE_REASON_pn-jpeg = "webkit-gtk 1.8.3 doesn't work with jpeg 9"
RECIPE_NO_UPDATE_REASON_pn-liberation-fonts = "2.x depends on fontforge package, which is not yet provided in oe-core"
RECIPE_NO_UPDATE_REASON_pn-pkgconfig = "removes glib-conf, adds circular depends"
RECIPE_NO_UPDATE_REASON_pn-python-pygobject = "Newer versions of python-pygobject depend on gobject-introspection which doesn't cross-compile"
RECIPE_NO_UPDATE_REASON_pn-webkit-gtk = ">= 1.10.2 needs ruby"
