# test result tool - report text based test results
#
# Copyright (c) 2019, Intel Corporation.
#
# This program is free software; you can redistribute it and/or modify it
# under the terms and conditions of the GNU General Public License,
# version 2, as published by the Free Software Foundation.
#
# This program is distributed in the hope it will be useful, but WITHOUT
# ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
# FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
# more details.
#
import os
import glob
import json
from resulttool.resultsutils import checkout_git_dir, load_json_file, get_dict_value, get_directory_files

class ResultsTextReport(object):

    def get_aggregated_test_result(self, logger, testresult):
        test_count_report = {'passed': 0, 'failed': 0, 'skipped': 0, 'failed_testcases': []}
        result_types = {'passed': ['PASSED', 'passed'],
                        'failed': ['FAILED', 'failed', 'ERROR', 'error', 'UNKNOWN'],
                        'skipped': ['SKIPPED', 'skipped']}
        result = get_dict_value(logger, testresult, 'result')
        for k in result:
            test_status = get_dict_value(logger, result[k], 'status')
            for tk in result_types:
                if test_status in result_types[tk]:
                    test_count_report[tk] += 1
            if test_status in result_types['failed']:
                test_count_report['failed_testcases'].append(k)
        return test_count_report

    def get_test_result_percentage(self, test_result_count):
        total_tested = test_result_count['passed'] + test_result_count['failed'] + test_result_count['skipped']
        test_percent_report = {'passed': 0, 'failed': 0, 'skipped': 0}
        for k in test_percent_report:
            test_percent_report[k] = format(test_result_count[k] / total_tested * 100, '.2f')
        return test_percent_report

    def add_test_configurations(self, test_report, source_dir, file, result_id):
        test_report['file_dir'] = self._get_short_file_dir(source_dir, file)
        test_report['result_id'] = result_id
        test_report['test_file_dir_result_id'] = '%s_%s' % (test_report['file_dir'], test_report['result_id'])

    def _get_short_file_dir(self, source_dir, file):
        file_dir = os.path.dirname(file)
        source_dir = source_dir[:-1] if source_dir[-1] == '/' else source_dir
        if file_dir == source_dir:
            return 'None'
        return file_dir.replace(source_dir, '')

    def get_max_string_len(self, test_result_list, key, default_max_len):
        max_len = default_max_len
        for test_result in test_result_list:
            value_len = len(test_result[key])
            if value_len > max_len:
                max_len = value_len
        return max_len

    def print_test_report(self, template_file_name, test_count_reports, test_percent_reports,
                          max_len_dir, max_len_result_id):
        from jinja2 import Environment, FileSystemLoader
        script_path = os.path.dirname(os.path.realpath(__file__))
        file_loader = FileSystemLoader(script_path + '/template')
        env = Environment(loader=file_loader, trim_blocks=True)
        template = env.get_template(template_file_name)
        output = template.render(test_count_reports=test_count_reports,
                                 test_percent_reports=test_percent_reports,
                                 max_len_dir=max_len_dir,
                                 max_len_result_id=max_len_result_id)
        print('Printing text-based test report:')
        print(output)

    def view_test_report(self, logger, source_dir, git_branch):
        if git_branch:
            checkout_git_dir(source_dir, git_branch)
        test_count_reports = []
        test_percent_reports = []
        for file in get_directory_files(source_dir, ['.git'], 'testresults.json'):
            logger.debug('Computing result for test result file: %s' % file)
            testresults = load_json_file(file)
            for k in testresults:
                test_count_report = self.get_aggregated_test_result(logger, testresults[k])
                test_percent_report = self.get_test_result_percentage(test_count_report)
                self.add_test_configurations(test_count_report, source_dir, file, k)
                self.add_test_configurations(test_percent_report, source_dir, file, k)
                test_count_reports.append(test_count_report)
                test_percent_reports.append(test_percent_report)
        max_len_dir = self.get_max_string_len(test_count_reports, 'file_dir', len('file_dir'))
        max_len_result_id = self.get_max_string_len(test_count_reports, 'result_id', len('result_id'))
        self.print_test_report('test_report_full_text.txt', test_count_reports, test_percent_reports,
                               max_len_dir, max_len_result_id)

def report(args, logger):
    report = ResultsTextReport()
    report.view_test_report(logger, args.source_dir, args.git_branch)
    return 0

def register_commands(subparsers):
    """Register subcommands from this plugin"""
    parser_build = subparsers.add_parser('report', help='report test result summary',
                                         description='report text-based test result summary from the source directory',
                                         group='analysis')
    parser_build.set_defaults(func=report)
    parser_build.add_argument('source_dir',
                              help='source directory that contain the test result files for reporting')
    parser_build.add_argument('-b', '--git-branch', default='',
                              help='(optional) default assume source directory contains all available files for '
                                   'reporting unless a git branch was provided where it will try to checkout '
                                   'the provided git branch assuming source directory was a git repository')
