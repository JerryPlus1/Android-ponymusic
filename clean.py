import os
import re

# 删除文件中的注释
def remove_comments_from_file(file_path):
    with open(file_path, 'r', encoding='utf-8') as file:
        content = file.read()

    # 删除单行注释 // 注释
    # content = re.sub(r'//.*', '', content)
    # 删除多行注释 /* 注释 */
    content = re.sub(r'/\*[\s\S]*?\*/', '', content)

    with open(file_path, 'w', encoding='utf-8') as file:
        file.write(content)

# 递归遍历项目目录
def process_directory(directory):
    for root, dirs, files in os.walk(directory):
        for file in files:
            # 查找Java、Kotlin、XML、Gradle文件
            if file.endswith(('.java', '.kt', '.xml')):
                file_path = os.path.join(root, file)
                remove_comments_from_file(file_path)
                print(f'Processed: {file_path}')

# 项目路径
project_dir = r'D:\AndroidProject\Android-ponymusic'

# 执行删除注释
process_directory(project_dir)
