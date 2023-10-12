# Checks related to the patch's author
#
# Copyright (C) 2016 Intel Corporation
#
# SPDX-License-Identifier: GPL-2.0

import base
import pyparsing

class Author(base.Base):

    auh_email = 'auh@auh.yoctoproject.org'

    invalids = [pyparsing.Regex("^Upgrade Helper.+"),
                pyparsing.Regex(auh_email),
                pyparsing.Regex("uh@not\.set"),
                pyparsing.Regex("\S+@example\.com")]


    def test_author_valid(self):
        for commit in self.commits:
            for invalid in self.invalids:
                if invalid.search_string(commit.author):
                    self.fail('Invalid author %s. Resend the series with a valid patch author' % commit.author, commit=commit)

    def test_non_auh_upgrade(self):
        for commit in self.commits:
            if self.auh_email in commit.payload:
                self.fail('Invalid author %s. Resend the series with a valid patch author' % self.auh_email, commit=commit)
