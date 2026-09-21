# FIXME: the LIC_FILES_CHKSUM values have been updated by 'devtool upgrade'.
# The following is the difference between the old and the new license text.
# Please update the LICENSE value if needed, and summarize the changes in
# the commit message via 'License-Update:' tag.
# (example: 'License-Update: copyright years updated.')
#
# The changes:
#
# --- COPYING
# +++ COPYING
# @@ -1,8 +1,8 @@
# -		  GNU LESSER GENERAL PUBLIC LICENSE
# -		       Version 2.1, February 1999
# +                  GNU LESSER GENERAL PUBLIC LICENSE
# +                       Version 2.1, February 1999
#  
#   Copyright (C) 1991, 1999 Free Software Foundation, Inc.
# -     51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
# + <https://fsf.org/>
#   Everyone is permitted to copy and distribute verbatim copies
#   of this license document, but changing it is not allowed.
#  
# @@ -10,7 +10,7 @@
#   as the successor of the GNU Library Public License, version 2, hence
#   the version number 2.1.]
#  
# -			    Preamble
# +                            Preamble
#  
#    The licenses for most software are designed to take away your
#  freedom to share and change it.  By contrast, the GNU General Public
# @@ -112,7 +112,7 @@
#  former contains code derived from the library, whereas the latter must
#  be combined with the library in order to run.
#  
# -		  GNU LESSER GENERAL PUBLIC LICENSE
# +                  GNU LESSER GENERAL PUBLIC LICENSE
#     TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION
#  
#    0. This License Agreement applies to any software library or other
# @@ -146,7 +146,7 @@
#  on the Library (independent of the use of the Library in a tool for
#  writing it).  Whether that is true depends on what the Library does
#  and what the program that uses the Library does.
# -  
# +
#    1. You may copy and distribute verbatim copies of the Library's
#  complete source code as you receive it, in any medium, provided that
#  you conspicuously and appropriately publish on each copy an
# @@ -432,7 +432,7 @@
#  of all derivatives of our free software and of promoting the sharing
#  and reuse of software generally.
#  
# -			    NO WARRANTY
# +                            NO WARRANTY
#  
#    15. BECAUSE THE LIBRARY IS LICENSED FREE OF CHARGE, THERE IS NO
#  WARRANTY FOR THE LIBRARY, TO THE EXTENT PERMITTED BY APPLICABLE LAW.
# @@ -455,7 +455,7 @@
#  SUCH HOLDER OR OTHER PARTY HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH
#  DAMAGES.
#  
# -		     END OF TERMS AND CONDITIONS
# +                     END OF TERMS AND CONDITIONS
#  
#             How to Apply These Terms to Your New Libraries
#  
# @@ -484,8 +484,7 @@
#      Lesser General Public License for more details.
#  
#      You should have received a copy of the GNU Lesser General Public
# -    License along with this library; if not, write to the Free Software
# -    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
# +    License along with this library; if not, see <https://www.gnu.org/licenses/>.
#  
#  Also add information on how to contact you by electronic and paper mail.
#  
# @@ -496,9 +495,7 @@
#    Yoyodyne, Inc., hereby disclaims all copyright interest in the
#    library `Frob' (a library for tweaking knobs) written by James Random Hacker.
#  
# -  <signature of Ty Coon>, 1 April 1990
# -  Ty Coon, President of Vice
# +  <signature of Moe Ghoul>, 1 April 1990
# +  Moe Ghoul, President of Vice
#  
#  That's all there is to it!
# -
# -
# 
#

SUMMARY = "Free Implementation of the Unicode Bidirectional Algorithm"
DESCRIPTION = "It provides utility functions to aid in the development \
of interactive editors and widgets that implement BiDi functionality. \
The BiDi algorithm is a prerequisite for supporting right-to-left scripts such \
as Hebrew, Arabic, Syriac, and Thaana. "
SECTION = "libs"
HOMEPAGE = "http://fribidi.org/"
BUGTRACKER = "https://github.com/fribidi/fribidi/issues"
LICENSE = "LGPL-2.1-or-later"
LIC_FILES_CHKSUM = "file://COPYING;md5=4bf661c1e3793e55c8d1051bc5e0ae21"

SRC_URI = "${GITHUB_BASE_URI}/download/v${PV}/${BP}.tar.xz \
           "
SRC_URI[sha256sum] = "6949dcde27d41cebad1fd741fcafc36d55a1020d2d872d4a6eb3914caabbada2"

inherit meson lib_package pkgconfig github-releases

CVE_PRODUCT = "gnu_fribidi fribidi"

BBCLASSEXTEND = "native nativesdk"
