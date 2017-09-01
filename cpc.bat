@echo off
set vars=(in\bloomington\timer in\bloomington\timer\bean in\bloomington\timer\util in\bloomington\timer\service in\bloomington\timer\list)

for %%i in %vars% do copy .\build\WEB-INF\classes\%%i\*.class .\WEB-INF\classes\%%i\.

